package ledennis.lib.inventoryserializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class InventorySerializer extends JavaPlugin {
	
	private static String newestVersion;
	
	@Override
	public void onEnable() {
		newestVersion = "v" + getDescription().getVersion();
	}
	
	private static Class<?> getExecutor(String version) {
		try {
			return Class.forName("ledennis.lib.inventoryserializer." + version.replaceAll("\\.", "_") + ".Executor");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void serialize(Inventory inv, File file) {
		Class<?> executor = getExecutor(newestVersion);
		try {
			Method method = executor.getDeclaredMethod("serialize", Inventory.class, File.class);
			method.invoke(null, inv, file);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public static void serialize(ItemStack[] items, PrintWriter out) {
		Class<?> executor = getExecutor(newestVersion);
		try {
			Method method = executor.getDeclaredMethod("serialize", ItemStack[].class, PrintWriter.class);
			method.invoke(null, items, out);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public static String serialize0(ItemStack item) {
		Class<?> executor = getExecutor(newestVersion);
		try {
			Method method = executor.getDeclaredMethod("serialize0", ItemStack.class);
			return method.invoke(null, item).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Inventory deserialize(File file) {
		try {
			String versionString = "";
			InputStream in = new FileInputStream(file);
			int b;
			while((b = in.read()) != ':') {
				versionString += (char) b;
			}
			in.close();
			Class<?> executor = getExecutor(versionString);
			Method method = executor.getDeclaredMethod("deserialize", File.class);
			return (Inventory) method.invoke(null, file);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
