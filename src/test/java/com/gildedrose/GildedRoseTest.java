package com.gildedrose;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GildedRoseTest {

    @Test
    @DisplayName("The Quality of an item is never negative")
    void negativeQualityTestTest () {
        //Given
        Item[] items = new Item[]{
            new Item("+5 Dexterity Vest", 10, 20), //
            new Item("Aged Brie", 2, 0), //
            new Item("Elixir of the Mongoose", 5, 7), //
            new Item("Sulfuras, Hand of Ragnaros", 0, 80), //
            new Item("Sulfuras, Hand of Ragnaros", -1, 80),
            new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),
            new Item("Backstage passes to a TAFKAL80ETC concert", 10, 49),
            new Item("Backstage passes to a TAFKAL80ETC concert", 5, 49),
            new Item("Conjured Mana Cake", 3, 6)};
        GildedRose app = new GildedRose(items);

        //When
        for (int i = 0; i < 5; i++) {
            app.updateQuality();
        }

        //Then
        assertFalse(Arrays.stream(items).sequential().anyMatch(x -> x.quality < 0));
        assertEquals(15, app.items[0].quality);
    }

    @Test
    @DisplayName("“Aged Brie“ actually increases in Quality the older it gets")
    void agedBrieIncreasesQualityTest () {
        //Given
        Item[] items = new Item[]{new Item("Aged Brie", 2, 0)};
        GildedRose app = new GildedRose(items);

        //When
        app.updateQuality();

        //Then
        assertTrue(app.items[0].quality > 0);
    }

    @Test
    @DisplayName("The Quality of an item is never more than 50")
    void qualityNotHigherThanFiftyTest () {
        //Given
        Item[] items = new Item[]{
            new Item("+5 Dexterity Vest", 10, 20), //
            new Item("Aged Brie", 2, 0), //
            new Item("Elixir of the Mongoose", 5, 7), //
            new Item("Sulfuras, Hand of Ragnaros", 0, 80), //
            new Item("Sulfuras, Hand of Ragnaros", -1, 80),
            new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),
            new Item("Backstage passes to a TAFKAL80ETC concert", 10, 49),
            new Item("Backstage passes to a TAFKAL80ETC concert", 5, 49),
            new Item("Conjured Mana Cake", 3, 6)};
        GildedRose app = new GildedRose(items);

        //When
        for (int i = 0; i < 5; i++) {
            app.updateQuality();
        }

        //Then
        assertTrue(Arrays.stream(items).sequential().anyMatch(x -> x.quality <= 50));
    }

    @Test
    @DisplayName("“Sulfuras”, being a legendary item, never has to be sold or decreases in Quality")
    void legendaryItemBehaviourTest () {
        //Given
        Item[] items = new Item[]{new Item("Sulfuras, Hand of Ragnaros", 2, 80)};
        GildedRose app = new GildedRose(items);

        //When
        app.updateQuality();

        //Then
        assertEquals(2, items[0].sellIn);
        assertEquals(80, items[0].quality);
    }

    @Test
    @DisplayName("“Backstage passes”, increases in Quality as it’s SellIn value approaches")
    void backstageIncreaseQualityTest () {
        //Given
        Item[] items = new Item[]{new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20)};
        GildedRose app = new GildedRose(items);

        //When
        for (int i = 0; i < 2; i++) {
            app.updateQuality();
        }

        //Then
        assertEquals(20 + 2, items[0].quality);
    }

    @Test
    @DisplayName("“Backstage passes”, increases in Quality as it’s SellIn value approaches. Quality increases by 2 when there are 10 days or less")
    void backstageIncreaseQualityFasterBetween10And2SellInTest () {
        //Given
        Item[] items = new Item[]{new Item("Backstage passes to a TAFKAL80ETC concert", 10, 20)};
        GildedRose app = new GildedRose(items);

        //When
        for (int i = 0; i < 2; i++) {
            app.updateQuality();
        }

        //Then
        assertEquals(20 + 4, items[0].quality);


        //Given
        items = new Item[]{new Item("Backstage passes to a TAFKAL80ETC concert", 9, 20)};
        app = new GildedRose(items);

        //When
        for (int i = 0; i < 2; i++) {
            app.updateQuality();
        }

        //Then
        assertEquals(20 + 4, items[0].quality);
    }


    @Test
    @DisplayName("“Backstage passes”, increases in Quality as it’s SellIn value approaches. Quality increases by 3 when there are 5 days or less")
    void backstageIncreaseQualityWith5SellInTest () {
        //Given
        Item[] items = new Item[]{new Item("Backstage passes to a TAFKAL80ETC concert", 5, 20)};
        GildedRose app = new GildedRose(items);

        //When
        for (int i = 0; i < 2; i++) {
            app.updateQuality();
        }

        //Then
        assertEquals(20 + 6, items[0].quality);

        //Given
        items = new Item[]{new Item("Backstage passes to a TAFKAL80ETC concert", 4, 20)};
        app = new GildedRose(items);

        //When
        for (int i = 0; i < 2; i++) {
            app.updateQuality();
        }

        //Then
        assertEquals(20 + 6, items[0].quality);
    }

    @Test
    @DisplayName("“Backstage passes”, Quality drops to 0 after the concert")
    void backstagePassQualityDropAfterSellInTest () {
        //Given
        Item[] items = new Item[]{new Item("Backstage passes to a TAFKAL80ETC concert", 5, 20)};
        GildedRose app = new GildedRose(items);

        //When
        for (int i = 0; i < 6; i++) {
            app.updateQuality();
        }

        //Then
        assertEquals(0, items[0].quality);
    }

    @Test
    @DisplayName("“Conjured” items degrade in Quality twice as fast as normal items")
    void qualityDegradationPositiveSellInConjuredTest () {
        Item[] items = new Item[]{new Item("Conjured", 3, 10)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(10 - 2, app.items[0].quality);
    }

    @Test
    @DisplayName("“Conjured” items degrade in Quality twice as fast as normal items")
    void qualityDegradationNegativeSellInConjuredTest () {
        Item[] items = new Item[]{new Item("Conjured", -5, 10)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(10 - 4, app.items[0].quality);
    }

    @Test
    @DisplayName("Quality degrades once per day")
    void qualityDegradationPositiveSellInNormalTest () {
        Item[] items = new Item[]{new Item("normal", 3, 10)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(10 - 1, app.items[0].quality);
    }

    @Test
    @DisplayName("Once the sell by date has passed, Quality degrades twice as fast")
    void qualityDegradationNegativeSellInNormalTest () {
        Item[] items = new Item[]{new Item("normal", -5, 10)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(10 - 2, app.items[0].quality);
    }

}
