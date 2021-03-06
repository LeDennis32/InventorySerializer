# InventorySerializer
API for saving &amp; loading inventories in files.
[German version](/README.de.md)<br><br>

Currently, special properties that only specific items have, like text of books, are not supported.<br>
They will be added in a future release.

## Usage
1. Saving inventories
```java
// Inventory, File
InventorySerializer.serialize(inventory, file);

// ItemStack[], PrintWriter
InventorySerializer.serialize(items, out);
```
2. Serializing single items
```java
// ItemStack
String str = InventorySerializer.serialize0(item);
```
3. Loading inventories
```java
// File
Inventory inv = InventorySerializer.deserialize(file);
```
You don't have to care about the version, old files are automatically loaded with the old api and saved with the newest version.
