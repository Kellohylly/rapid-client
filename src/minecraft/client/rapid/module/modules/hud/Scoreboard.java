package client.rapid.module.modules.hud;

import client.rapid.Wrapper;
import client.rapid.module.Draggable;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

@ModuleInfo(getName = "Scoreboard", getCategory = Category.HUD)
public class Scoreboard extends Draggable {

    public Scoreboard() {
        super(40, 200, 110, 130);
    }

    public void renderScoreboard(ScoreObjective p_180475_1_, ScaledResolution p_180475_2_)
    {
        net.minecraft.scoreboard.Scoreboard scoreboard = p_180475_1_.getScoreboard();
        Collection collection = scoreboard.getSortedScores(p_180475_1_);
        ArrayList arraylist = Lists.newArrayList(Iterables.filter(collection, new Predicate()
        {
            private static final String __OBFID = "CL_00001958";
            public boolean apply(Score p_apply_1_)
            {
                return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
            }
            public boolean apply(Object p_apply_1_)
            {
                return this.apply((Score)p_apply_1_);
            }
        }));
        ArrayList arraylist1;

        if (arraylist.size() > 15)
        {
            arraylist1 = Lists.newArrayList(Iterables.skip(arraylist, collection.size() - 15));
        }
        else
        {
            arraylist1 = arraylist;
        }

        int i = mc.fontRendererObj.getStringWidth(p_180475_1_.getDisplayName());

        for (Object score0 : arraylist1)
        {
            Score score = (Score) score0;
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
            i = Math.max(i, mc.fontRendererObj.getStringWidth(s));
        }

        int j1 = arraylist1.size() * mc.fontRendererObj.FONT_HEIGHT;
        int k1 = y + height - 40 + j1 / 3;
        byte b0 = 3;
        int j = x + width - i - b0;
        int k = 0;

        for (Object score10 : arraylist1)
        {
            Score score1 = (Score) score10;
            ++k;
            ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
            String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
            String s2 = EnumChatFormatting.RED + "" + score1.getScorePoints();
            int l = k1 - k * mc.fontRendererObj.FONT_HEIGHT;
            int i1 = x + width - b0 + 2;
            Gui.drawRect(j - 2, l, i1, l + mc.fontRendererObj.FONT_HEIGHT, new Color(0, 0, 0, (int) Wrapper.getSettingsManager().getSettingByName("Hud Settings", "Opacity").getValue()).getRGB());
            mc.fontRendererObj.drawString(s1, j, l, 553648127);
            mc.fontRendererObj.drawString(s2, i1 - mc.fontRendererObj.getStringWidth(s2), l, 553648127);

            if (k == arraylist1.size())
            {
                String s3 = p_180475_1_.getDisplayName();
                Gui.drawRect(j - 2, l - mc.fontRendererObj.FONT_HEIGHT - 1, i1, l - 1, 1610612736);
                Gui.drawRect(j - 2, l - 1, i1, l, 1342177280);
                mc.fontRendererObj.drawString(s3, j + i / 2 - mc.fontRendererObj.getStringWidth(s3) / 2, l - mc.fontRendererObj.FONT_HEIGHT, 553648127);
            }
        }
    }
}
