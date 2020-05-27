package ledennis.lib.inventoryserializer.v1_1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Executor {
	
	private static final String VERSION_KEY = "v1.1";
	
	public static void serialize(Inventory inventory, File file) throws IOException {
		if(!file.exists()) file.createNewFile();
		if(file.isDirectory()) return;
		serialize(inventory.getContents(), new PrintWriter(file));
	}
	
	public static void serialize(ItemStack[] items, PrintWriter out) {
		String str = "";
		str += VERSION_KEY + ":";
		for(ItemStack item : items) {
			str += serialize0(item);
			str += "@";
		}
		str = str.substring(0, str.length() - 1);
		out.println(str);
		out.flush();
		out.close();
	}
	
	@SuppressWarnings("deprecation")
	public static String serialize0(ItemStack item) {
		if(item == null) return "$NONE";
		String str = MessageFormat.format("{0}:{1}:{2};", 
				item.getTypeId(), 
				item.getDurability(), 
				item.getAmount());
		str = str.replaceAll(",", "");
		
		if(item.getEnchantments().isEmpty()) {
			str += "$NONE";
		} else {
			for(Entry<Enchantment, Integer> e : item.getEnchantments().entrySet()) {
				str += e.getKey().getId() + "#" + e.getValue() + ":";
			}
			str = str.substring(0, str.length() - 1);
		}
		
		str += ";";
		
		ItemMeta meta = item.getItemMeta();
		
		if(!meta.hasDisplayName()) {
			str += "$NONE:";
		} else {
			String name = formatString(meta.getDisplayName());
			str += name + ":";
		}
		
		if(!meta.hasLore()) {
			str += "$NONE";
		} else {
			List<String> lore = meta.getLore();
			for(String line : lore) {
				str += formatString(line) + "~";
			}
			str = str.substring(0, str.length() - 1);
		}
		
		return str;
	}
	
	@SuppressWarnings("deprecation")
	public static Inventory deserialize(File file) {
		if(!file.exists()) return null;
		if(file.isDirectory()) return null;
		
		Scanner in;
		try {
			in = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		if(!in.hasNextLine()) {
			in.close();
			return null;
		}
		
		String str = in.nextLine();
		
		in.close();
		
		if(str == null) return null;
		if(str.equals("")) return null;
		
		str = str.substring(VERSION_KEY.length() + 1);
		
		String[] data = str.split("@");
		ItemStack[] content = new ItemStack[data.length];
		
		for(int i = 0; i < data.length; i++) {
			String s = data[i];
			ItemStack item;
			if(s.equals("$NONE")) {
				content[i] = null;
			} else {
				String[] var0 = s.split(";");
				String general = var0[0];
				String ench = var0[1];
				String meta = var0[2];
				// GENERAL
				int[] var1 = toIntArray(general.split(":"));
				item = new ItemStack(var1[0], var1[2], (short) var1[1]);
				// ENCHANMENTS
				if(!ench.equals("$NONE")) {
					String[] var2 = ench.split(":");
					for(String s981 : var2) {
						int[] t981 = toIntArray(s981.split("#"));
						item.addUnsafeEnchantment(Enchantment.getById(t981[0]), t981[1]);
					}
				}
				// META
				ItemMeta imeta = item.getItemMeta();
				String[] var3 = meta.split(":");
				if(!var3[0].equals("$NONE")) imeta.setDisplayName(var3[0]);
				if(!var3[1].equals("$NONE")) {
					List<String> lore = new ArrayList<>();
					String[] t154 = var3[1].split("~");
					for(String line : t154) {
						lore.add(line);
					}
					imeta.setLore(lore);
				}
				item.setItemMeta(imeta);
				// FINISH
				content[i] = item;
			}
		}
		
		Inventory inv = Bukkit.createInventory(null, content.length);
		inv.setContents(content);
		return inv;
	}
	
	private static int[] toIntArray(String[] in) {
		int[] out = new int[in.length];
		for(int i = 0; i < in.length; i++) {
			out[i] = Integer.parseInt(in[i]);
		}
		return out;
	}
	
	private static String formatString(String in) {
		char[] forbidden = {
				';',
				':',
				'~',
				'@'
		};
		for(char c : forbidden) {
			if(in.contains(String.valueOf(c))) {
				in = in.replace(c, ' ');
			}
		}
		return in;
	}
	
}
