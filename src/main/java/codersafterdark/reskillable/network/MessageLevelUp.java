package codersafterdark.reskillable.network;

import codersafterdark.reskillable.base.*;
import codersafterdark.reskillable.skill.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class MessageLevelUp implements IMessage, IMessageHandler<MessageLevelUp, IMessage> {
    
    public String skill;
    
    public MessageLevelUp() {
    }
    
    public MessageLevelUp(String skill) {
        this.skill = skill;
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        skill = ByteBufUtils.readUTF8String(buf);
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, skill);
    }
    
    @Override
    public IMessage onMessage(MessageLevelUp message, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> handleMessage(message, ctx));
        return null;
    }
    
    public IMessage handleMessage(MessageLevelUp message, MessageContext context) {
        EntityPlayer player = context.getServerHandler().player;
        Skill skill = Skills.SKILLS.get(message.skill);
        PlayerData data = PlayerDataHandler.get(player);
        PlayerSkillInfo info = data.getSkillInfo(skill);
        if(!info.isCapped()) {
            int cost = info.getLevelUpCost();
            if(player.experienceLevel >= cost || player.isCreative()) {
                if(!player.isCreative()) {
                    player.addExperienceLevel(-cost);
                }
                info.levelUp();
                data.saveAndSync();
            }
        }
        return null;
    }
    
    
}
