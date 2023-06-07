package com.gildedrose;

class GildedRose {
    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
        for (Item item : items) {
            if (isSpecialItem(item, Constants.LEGENDARY)) {
                continue;
            }
            if (isSpecialItem(item, Constants.AGED_BRIE)) {
                updateAgedBrieQuality(item);
            } else if (isSpecialItem(item, Constants.BACKSTAGE_PASS)) {
                updateBackstagePassesQuality(item);
            } else if (isSpecialItem(item, Constants.CONJURER)) {
                updateConjuredItemQuality(item);
            } else {
                updateNormalItemQuality(item);
            }
            fixItemQuality(item);
            item.sellIn--;
        }
    }

    private static boolean isSpecialItem(Item item, String typeName) {
        String itemName = item.name != null && !"".equals(item.name) ? item.name.toLowerCase() : "";
        return itemName.contains(typeName) || itemName.startsWith(typeName);
    }

    private void updateAgedBrieQuality(Item item) {
        if (item.sellIn <= 0) {
            item.quality += 2;
        } else {
            item.quality++;
        }
    }

    private void updateBackstagePassesQuality(Item item) {
        if (item.sellIn <= 0) {
            item.quality = 0;
        } else if (item.sellIn <= 5) {
            item.quality += 3;
        } else if (item.sellIn <= 10) {
            item.quality += 2;
        } else {
            item.quality++;
        }
    }

    private void updateConjuredItemQuality(Item item) {
        if (item.sellIn <= 0) {
            item.quality -= 4;
        } else {
            item.quality -= 2;
        }
    }

    private void updateNormalItemQuality(Item item) {
        if (item.sellIn <= 0) {
            item.quality -= 2;
        } else {
            item.quality--;
        }
    }

    private void fixItemQuality(Item item) {
        if (item.quality < Constants.MIN_QUALITY) {
            item.quality = Constants.MIN_QUALITY;
        } else if (item.quality > Constants.MAX_QUALITY) {
            item.quality = Constants.MAX_QUALITY;
        }
    }

}
