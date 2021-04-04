package com.animationtransmog.effect;
/*
Given an actor, the Effect class can control the gfx, animation, and the timing
of the two to orchestrate and overall visual effect.
 */

import com.animationtransmog.AnimationTypes;
import com.animationtransmog.config.AnimationTransmogConfigManager;
import net.runelite.api.Actor;

import java.util.HashMap;

public class EffectController {
    AnimationTypes animationTypes;
    AnimationTransmogConfigManager configManager;

    HashMap<String, Effect> effects;

    public int currentAnimationId = -1;
    public int currentGfxId = -1;

    public Actor actor = null;

    public EffectController(AnimationTypes animationTypes, AnimationTransmogConfigManager configManager)
    {
        this.animationTypes = animationTypes;
        this.configManager = configManager;

        // Defining Effects
        effects = new HashMap<>();

        // Teleport Effects
        effects.put("Darkness Ascends", new Effect(3945, 1577));
        effects.put("2010 Vibes", new Effect(3945, 56));
        effects.put("Jad 2 OP", new Effect(836, 451));

        // Action Effects
        effects.put("Arcane Chop", new Effect(6298, 1063));
        effects.put("Arcane Mine", new Effect(4411, 739));
        effects.put("Blast Mine", new Effect(2107, 659));
        effects.put("Dig", new Effect(830, -1));
        effects.put("Headbang", new Effect(2108, -1));
    }

    public void setPlayer(Actor actor)
    {
        this.actor = actor;
    }

    public void update()
    {
        int currentAnimation = actor.getAnimation();
        String currentAnimationType = animationTypes.getAnimationType(currentAnimation);
        if (currentAnimationType == null)
        {
            if (currentGfxId != -1)
            {
                actor.setGraphic(-1);
                currentGfxId = -1;
            }
            return;
        }

        String configOption = configManager.getConfigOption(currentAnimationType);
        if (configOption.equals("Default")) return;

        Effect effect = getEffect(configOption);
        if (effect == null) return;

        Animation newAnimation = effect.animation;
        GFX newGfx = effect.gfx;

        currentAnimationId = newAnimation.animationId;
        actor.setAnimation(newAnimation.animationId);
        actor.setActionFrame(newAnimation.startFrame);

        if (newGfx.gfxId != -1)
        {
            currentGfxId = newGfx.gfxId;
            actor.setGraphic(newGfx.gfxId);
            actor.setSpotAnimFrame(newGfx.startFrame);
        }
    }

    public void override()
    {
        int currentGfx = actor.getGraphic();

        // If gfx is play and client tries to override it, re-override it with effect gfx
        if (currentGfxId != -1 && currentGfx != currentGfxId)
        {
            actor.setGraphic(currentGfxId);
        }
    }

    // Gets an Effect given the name of the effect
    Effect getEffect(String effectName)
    {
        return effects.get(effectName);
    }
}