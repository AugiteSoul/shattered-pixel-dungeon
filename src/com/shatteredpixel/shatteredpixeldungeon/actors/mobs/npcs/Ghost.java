/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import java.util.HashSet;

import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Crab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Gnoll;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.CurareDart;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrabSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Journal;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.RatSkull;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerLevel;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FetidRatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSadGhost;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

//TODO: test this whole class a bunch
public class Ghost extends Mob.NPC {

	{
		name = "sad ghost";
		spriteClass = GhostSprite.class;
		
		flying = true;
		
		state = State.WANDERING;
	}
	
	private static final String TXT_RAT1	=
            "Hello adventurer... Once I was like you - strong and confident... " +
            "But I was slain by a foul beast... I can't leave this place... Not until I have my revenge... " +
            "Slay the _fetid rat_, that has taken my life...\n\n" +
            "It stalks this floor... Spreading filth everywhere... " +
            "Beware its cloud of stink and acidic bite... ";

    private static final String TXT_RAT2	=
            "Please... Help me... Slay the abomination...";

    private static final String TXT_GNOLL1	=
            "Hello adventurer... Once I was like you - strong and confident... " +
            "But I was slain by a devious foe... I can't leave this place... Not until I have my revenge... " +
            "Slay the _gnoll trickster_, that has taken my life...\n\n" +
            "It is not like the other gnolls... It hides and uses thrown weapons... " +
            "Beware its poisonous and incendiary darts... ";

    private static final String TXT_GNOLL2	=
            "Please... Help me... Slay the trickster...";

    private static final String TXT_CRAB1	=
            "Hello adventurer... Once I was like you - strong and confident... " +
            "But I was slain by an ancient creature... I can't leave this place... Not until I have my revenge... " +
            "Slay the _great crab_, that has taken my life...\n\n" +
            "It is unnaturally old... With a massive single claw and a thick shell. " +
            "Beware its claw, the crab blocks and strikes with it...";

    private static final String TXT_CRAB2	=
            "Please... Help me... Slay the abomination...";
	
	public Ghost() {
		super();

		Sample.INSTANCE.load( Assets.SND_GHOST );
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		return 1000;
	}
	
	@Override
	public String defenseVerb() {
		return "evaded";
	}
	
	@Override
	public float speed() {
		return 0.5f;
	}
	
	@Override
	protected Char chooseEnemy() {
		return DUMMY;
	}
	
	@Override
	public void damage( int dmg, Object src ) {
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public void interact() {
		sprite.turnTo( pos, Dungeon.hero.pos );
		
		Sample.INSTANCE.play( Assets.SND_GHOST );
		
		if (Quest.given) {
			
			if (Quest.processed){
				GameScene.show( new WndSadGhost( this, Quest.type ) );
			} else {
                switch (Quest.type){
                    case 1: default:
                        GameScene.show( new WndQuest( this, TXT_RAT2 ) ); break;
                    case 2:
                        GameScene.show( new WndQuest( this, TXT_GNOLL2 ) ); break;
                    case 3:
                        GameScene.show( new WndQuest( this, TXT_CRAB2 ) ); break;
                }
				
				int newPos = -1;
				for (int i=0; i < 10; i++) {
					newPos = Dungeon.level.randomRespawnCell();
					if (newPos != -1) {
						break;
					}
				}
				if (newPos != -1) {
					
					Actor.freeCell( pos );
					
					CellEmitter.get( pos ).start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
					pos = newPos;
					sprite.place( pos );
					sprite.visible = Dungeon.visible[pos];
				}
			}
			
		} else {
            Mob questBoss;
            String txt_quest;

            switch (Quest.type){
                case 1: default:
                    questBoss = new FetidRat();
                    txt_quest = TXT_RAT1; break;
                case 2:
                    questBoss = new GnollTrickster();
                    txt_quest = TXT_GNOLL1; break;
                case 3:
                    questBoss = new GreatCrab();
                    txt_quest = TXT_CRAB1; break;
            }

            questBoss.state = Mob.State.WANDERING;
            questBoss.pos = Dungeon.level.randomRespawnCell();

            if (questBoss.pos != -1) {
                GameScene.add(questBoss);
                GameScene.show( new WndQuest( this, txt_quest ) );
                Quest.given = true;
                Journal.add( Journal.Feature.GHOST );
            }

		}
	}
	
	@Override
	public String description() {
		return 
			"The ghost is barely visible. It looks like a shapeless " +
			"spot of faint light with a sorrowful face.";
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Paralysis.class );
		IMMUNITIES.add( Roots.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
	
	public static class Quest {
		
		private static boolean spawned;

        private static int type;

		private static boolean given;
		
		private static boolean processed;
		
		private static int depth;
		
		public static Weapon weapon;
		public static Armor armor;
		
		public static void reset() {
			spawned = false; 
			
			weapon = null;
			armor = null;
		}
		
		private static final String NODE		= "sadGhost";
		
		private static final String SPAWNED		= "spawned";
        private static final String TYPE        = "type";
		private static final String GIVEN		= "given";
		private static final String PROCESSED	= "processed";
		private static final String DEPTH		= "depth";
		private static final String WEAPON		= "weapon";
		private static final String ARMOR		= "armor";

        //for pre-0.2.1 saves, used when restoring quest
        private static final String ALTERNATIVE	= "alternative";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				
				node.put( TYPE, type );
				
				node.put( GIVEN, given );
				node.put( DEPTH, depth );
				node.put( PROCESSED, processed);
				
				node.put( WEAPON, weapon );
				node.put( ARMOR, armor );
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {
			
			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && !node.contains( ALTERNATIVE ) && (spawned = node.getBoolean( SPAWNED ))) {
				
				type = node.getInt( TYPE );

				given	= node.getBoolean( GIVEN );
				depth	= node.getInt( DEPTH );
				processed = node.getBoolean( PROCESSED );
				
				weapon	= (Weapon)node.get( WEAPON );
				armor	= (Armor)node.get( ARMOR );
			} else {
				reset();
			}
		}
		
		public static void spawn( SewerLevel level ) {
			if (!spawned && Dungeon.depth > 1 && Random.Int( 5 - Dungeon.depth ) == 0) {
				
				Ghost ghost = new Ghost();
				do {
					ghost.pos = level.randomRespawnCell();
				} while (ghost.pos == -1);
				level.mobs.add( ghost );
				Actor.occupyCell( ghost );
				
				spawned = true;
                //dungeon depth determines type of quest.
                //depth2=fetid rat, 3=gnoll trickster, 4=great crab
				type = Dungeon.depth-1;
				
				given = false;
				processed = false;
				depth = Dungeon.depth;

                //TODO: test and decide on balancing for this
                do {
                    weapon = Generator.randomWeapon(10);
                } while (weapon instanceof MissileWeapon);
                armor = Generator.randomArmor(10);

                for (int i = 1; i <= 3; i++) {
                    Item another;
                    do {
                        another = Generator.randomWeapon(10+i);
                    } while (another instanceof MissileWeapon);
                    if (another.level > weapon.level) {
                        weapon = (Weapon) another;
                    }
                    another = Generator.randomArmor(10+i);
                    if (another.level > armor.level) {
                        armor = (Armor) another;
                    }
                }

				weapon.identify();
				armor.identify();
			}
		}
		
		public static void process() {
			if (spawned && given && !processed && (depth == Dungeon.depth)) {
				GLog.p("Thank you... come find me...");
                Sample.INSTANCE.play( Assets.SND_GHOST );
                processed = true;
			}
		}
		
		public static void complete() {
			weapon = null;
			armor = null;
			
			Journal.remove( Journal.Feature.GHOST );
		}
	}
	
	public static class FetidRat extends Mob {

		{
			name = "fetid rat";
			spriteClass = FetidRatSprite.class;
			
			HP = HT = 20;
			defenseSkill = 4;
			
			EXP = 4;
		}
		
		@Override
		public int damageRoll() {
			return Random.NormalIntRange( 1, 4 );
		}
		
		@Override
		public int attackSkill( Char target ) {
			return 12;
		}
		
		@Override
		public int dr() {
			return 2;
		}

        @Override
        public int attackProc( Char enemy, int damage ) {
            if (Random.Int( 3 ) == 0) {
                Buff.affect(enemy, Ooze.class);
            }

            return damage;
        }
		
		@Override
		public int defenseProc( Char enemy, int damage ) {
			
			GameScene.add( Blob.seed( pos, 20, ParalyticGas.class ) );
			
			return super.defenseProc(enemy, damage);
		}
		
		@Override
		public void die( Object cause ) {
			super.die( cause );
			
			Quest.process();
		}
		
		@Override
		public String description() {
			return
				"Something is clearly wrong with this rat. Its matted fur and rotting skin are very " +
                "different from the healthy rats you've seen previously. It's bright orange eyes " +
                "and larger size make it especially menacing.\n\n" +
                "The rat carries a cloud of horrible stench with it, it's overpoweringly strong up close.\n\n" +
                "Dark ooze drips from the rat's teeth, it eats through the floor but seems to dissolve in water.";
		}
		
		private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
		static {
			IMMUNITIES.add( Paralysis.class );
		}
		
		@Override
		public HashSet<Class<?>> immunities() {
			return IMMUNITIES;
		}
	}

    public static class GnollTrickster extends Gnoll {
        {
            name = "gnoll trickster";
            spriteClass = GnollSprite.class;

            HP = HT = 20;
            defenseSkill = 4;

            EXP = 5;

            loot = Generator.random(CurareDart.class);
            lootChance = 1f;
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange( 1, 4 );
        }

        @Override
        public int attackSkill( Char target ) {
            return 14;
        }

        @Override
        protected boolean canAttack( Char enemy ) {
            return !Level.adjacent(pos, enemy.pos) && Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
        }

        @Override
        public int attackProc( Char enemy, int damage ) {
                if (Random.Int(3) == 0) {
                    if (Level.flamable[enemy.pos]) {
                        GameScene.add(Blob.seed(enemy.pos, 4, Fire.class));
                    }
                    Buff.affect( enemy, Burning.class ).reignite( enemy );
                } else if (HP <= 8){
                    Buff.prolong( enemy, Cripple.class, 2 );
                }

            return damage;
        }

        @Override
        protected boolean getCloser( int target ) {
            if (state == State.HUNTING) {
                return enemySeen && getFurther( target );
            } else {
                return super.getCloser( target );
            }
        }

        @Override
        public void die( Object cause ) {
            super.die( cause );

            Quest.process();
        }

        @Override
        public String description() {
            return
                    "A Gnoll";
        }

    }

    public static class GreatCrab extends Crab {
        {
            name = "great crab";
            spriteClass = CrabSprite.class;

            HP = HT = 30;
            defenseSkill = 100; //no that's not a typo
            baseSpeed = 0.67f;

            EXP = 6;

        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange( 4, 6 );
        }

        @Override
        public int attackSkill( Char target ) {
            return 13;
        }

        @Override
        public int dr() {
            return 4;
        }

        @Override
        public void die( Object cause ) {
            super.die( cause );

            Quest.process();

            Dungeon.level.drop( new MysteryMeat(), pos );
            Dungeon.level.drop( new MysteryMeat(), pos );
            Dungeon.level.drop( new MysteryMeat(), pos ).sprite.drop();
        }

        @Override
        public String description() {
            return
                    "A Crab";
        }
    }
}
