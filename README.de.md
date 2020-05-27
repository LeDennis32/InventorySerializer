# InventorySerializer
API zum speichern &amp; laden von Inventaren in Dateien.
[English Version](/README.md)<br><br>

Momentan werden spezielle Eigenschafen, die nur spezifische Items haben, wie Text von Büchern, nicht unterstützt.<br>
Diese werden in einer zukünftigen Version hinzugefügt.

## Verwendung
1. Inventare speichern
```java
// Inventory, File
InventorySerializer.serialize(inventory, file);

// ItemStack[], PrintWriter
InventorySerializer.serialize(items, out);
```
2. Einzelne Items serialisieren
```java
// ItemStack
String str = InventorySerializer.serialize0(item);
```
3. Inventare laden
```java
// File
Inventory inv = InventorySerializer.deserialize(file);
```
Du musst dich nicht um die Version kümmern, alte Dateien werden automatisch mit der alten API geladen und mit der neusten Version gespeichert.
