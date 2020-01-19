/*
 * Copyright (c) 2018, Seth <Sethtroll3@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.blastfurnace;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Client;
import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;

import net.runelite.api.Point;
import net.runelite.api.Varbits;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;

import static net.runelite.api.Varbits.BAR_DISPENSER;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

class BlastFurnaceTotalsOverlay extends Overlay
{
    private final Client client;
    private final BlastFurnacePlugin plugin;
    private final PanelComponent imagePanelComponent = new PanelComponent();
    private HashMap<Bars, Integer> barsMade = new HashMap<Bars, Integer>();
    private boolean isBarsReady = false;

    @Inject
    private ItemManager itemManager;

    @Inject
    BlastFurnaceTotalsOverlay(Client client, BlastFurnacePlugin plugin)
    {
        super(plugin);
        this.plugin = plugin;
        this.client = client;
        setPosition(OverlayPosition.TOP_LEFT);
        imagePanelComponent.setOrientation(ComponentOrientation.HORIZONTAL);
        getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "Blast furnace totals overlay"));
    }

    public void clearTotals()
    {
        barsMade.clear();
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (plugin.getConveyorBelt() == null)
        {
            return null;
        }

        int totalProfit = 0;

        imagePanelComponent.getChildren().clear();

        int barDispenser = client.getVar(BAR_DISPENSER);

        for (Bars varbit : Bars.values()) {
            int amount = client.getVar(varbit.getVarbit());

            if(( barDispenser == 2 || barDispenser == 3 ) && !isBarsReady) {
                // Update total
                int totalMade = barsMade.getOrDefault(varbit, 0) + amount;
                barsMade.put(varbit, totalMade);

                if (totalMade == 0) {
                    continue;
                }
            }

            int itemPrice = itemManager.getItemPrice(varbit.getItemID());
            int totalMade = barsMade.getOrDefault(varbit, 0);

            int totalPrice = itemPrice * totalMade;
            int totalCost = (itemManager.getItemPrice(BarsOres.COAL.getItemID()) * 2 + itemManager.getItemPrice(Bars.MITHRIL_BAR.getItemID())) * totalMade;

            totalProfit += totalCost - totalPrice;

            if (totalMade > 0) {
                imagePanelComponent.getChildren().add(new ImageComponent(getImage(varbit.getItemID(), totalMade)));
            }
        }

        if (barDispenser == 2 || barDispenser == 3) {
            isBarsReady = true;
        } else {
            isBarsReady = false;
        }

        imagePanelComponent.getChildren().add(LineComponent.builder()
                .left("     Profit: ")
                .leftColor(Color.ORANGE)
                .right(String.valueOf(totalProfit) + " gp")
                .build());

        return imagePanelComponent.render(graphics);
    }

    private BufferedImage getImage(int itemID, int amount)
    {
        BufferedImage image = itemManager.getImage(itemID, amount, true);
        return image;
    }
}
