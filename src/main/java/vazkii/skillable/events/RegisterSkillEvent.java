package vazkii.skillable.events;

import net.minecraftforge.fml.common.eventhandler.Event;
import vazkii.skillable.skill.Skills;

public class RegisterSkillEvent extends Event {
    private Skills skills;

    public RegisterSkillEvent(Skills skills) {
        this.skills = skills;
    }

    public Skills getSkills(){
        return this.skills;
    }
}
