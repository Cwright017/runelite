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

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import lombok.Getter;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;

public enum Bars
{
    BRONZE_BAR(Varbits.BLAST_FURNACE_BRONZE_BAR, ItemID.BRONZE_BAR),
    IRON_BAR(Varbits.BLAST_FURNACE_IRON_BAR, ItemID.IRON_BAR),
    STEEL_BAR(Varbits.BLAST_FURNACE_STEEL_BAR, ItemID.STEEL_BAR),
    MITHRIL_BAR(Varbits.BLAST_FURNACE_MITHRIL_BAR, ItemID.MITHRIL_BAR),
    ADAMANTITE_BAR(Varbits.BLAST_FURNACE_ADAMANTITE_BAR, ItemID.ADAMANTITE_BAR),
    RUNITE_BAR(Varbits.BLAST_FURNACE_RUNITE_BAR, ItemID.RUNITE_BAR),
    SILVER_BAR(Varbits.BLAST_FURNACE_SILVER_BAR, ItemID.SILVER_BAR),
    GOLD_BAR(Varbits.BLAST_FURNACE_GOLD_BAR, ItemID.GOLD_BAR);

    private static final Map<Varbits, Bars> VARBIT;

    static
    {
        ImmutableMap.Builder<Varbits, Bars> builder = new ImmutableMap.Builder<>();

        for (Bars s : values())
        {
            builder.put(s.getVarbit(), s);
        }

        VARBIT = builder.build();
    }

    @Getter
    private final Varbits varbit;
    @Getter
    private final int itemID;

    Bars(Varbits varbit, int itemID)
    {
        this.varbit = varbit;
        this.itemID = itemID;
    }

    public static Bars getVarbit(Varbits varbit)
    {
        return VARBIT.get(varbit);
    }
}
