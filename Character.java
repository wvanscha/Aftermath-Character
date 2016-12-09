import java.util.*;
import java.io.*;

public class Character {
	
	String name;
	int level;
	int xp;
	String Class;
	String background;
	String training;
	String trait;
	//attributes
	int str;
	int dex;
	int con;
	int Int;
	int wis;
	int cha;
	//health
	int maxhp;
	int curhp;
	
	double encumbrance;
	//combat
	int ab;
	int ac;
	//throws
	int physical;
	int mental;
	int evade;
	int luck;
	int tech;
	
	
	ArrayList<Equipment> equip;
	ArrayList<Skill> skills;
	ArrayList<String> skillsAdded;
	
	// Constructor gives basic equipment and stats, only useful for creating a new character object and not loading one
	public Character () {
		equip = new ArrayList<Equipment>();
		skills = new ArrayList<Skill>();
		skillsAdded = new ArrayList<String>();
		
		try (BufferedReader br = new BufferedReader(new FileReader("skills.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
			   skills.add( new Skill(line) );
			}
		} catch ( Exception E ) {
			System.out.println("Error reading skills list\n");
			E.printStackTrace();
		}
		
		equip.add(new Equipment( "Flint Knife", 1, 1, 0, "1d6", "0", 0 ) );
		equip.add(new Equipment( "Ration", 0.33, 2 + roll( 1, 6 ) ) );
		str = roll(3,6);
		dex = roll(3,6);
		con = roll(3,6);
		Int = roll(3,6);
		wis = roll(3,6);
		cha = roll(3,6);
	}
		
	public int getMod ( int attribute ) {
		if ( attribute == 18 ) {
			return 2; 
		} else if ( attribute >= 14 ) {
			return 1;
		} else if ( attribute == 3 ) {
			return -2;
		} else if ( attribute <= 7 ) {
			return -1;
		} else {
			return 0;
		}
	}
	
	// Helper function simulating dice
	public int roll ( int times, int faces ) {
		int total = 0;
		Random rand = new Random();
		for ( int i = 0; i < times; i ++ ) {
			total += rand.nextInt( faces ) + 1;
		}
		
		return total;
	}
	
	double getTotalEncumbrance () {
		int ret = 0;
		for ( int i = 0; i < equip.size(); i++ ) {
			ret += equip.get(i).totalEnc();
		}
		return ret;
	}
	
	int getTotalAC () {
		int ret = 0;
		for ( int i = 0; i < equip.size(); i++ ) {
			if ( equip.get(i).isArmor() ) {
				ret += equip.get(i).getAC();
			}
		}
		return ret;
	}
	
	// Strings character data similar to charsheet
	public String toString () {
		String ret = "\n";
		ret += "Name: " + name + " \tLvl: " + level + " \tXP: " + xp + "\n";
		ret += "Class: " + Class + " \tBackground: " + background + "\n";
		ret += "Training: " + training + " \tTrait: " + trait + "\n";
		ret += "\nAttributes\n";
		ret += "Str: " + str + "\t" + getMod(str) + "\n";
		ret += "Dex: " + dex + "\t" + getMod(dex) + "\n";
		ret += "Con: " + con + "\t" + getMod(con) + "\n";
		ret += "Int: " + Int + "\t" + getMod(Int) + "\n";
		ret += "Wis: " + wis + "\t" + getMod(wis) + "\n";
		ret += "Cha: " + cha + "\t" + getMod(cha) + "\n";
		ret += "\nHP - max: " + maxhp + " \tcurrent: " + curhp + " \tEncumbrance: " + getTotalEncumbrance() + "\n";
		ret += "Attack Bonus: " + ab + " \tArmor Class : " + ac + "\n";
		ret += "\nArmor\t\t\tAC\n";
		for ( int i = 0; i < equip.size(); i++ ) {
			if( equip.get(i).isArmor ) {
				ret += equip.get(i).toArmor() + "\n";
			}
		} 
		
		ret += "\nWeapons\t\tHit\tDmg\tRange\tAmmo\n";
		for ( int i = 0; i < equip.size(); i++ ) {
			if( equip.get(i).isWeapon ) {
				ret += equip.get(i).toWeapon() + "\n";
			}
		} 
		
		ret += "\nEquipment\tEnc\n";
		for ( int i = 0; i < equip.size(); i++ ) {
			ret += equip.get(i) + "\n";
		} 
		
		ret += "\nSkills\tRank\n";
		for ( int i = 0; i < skills.size(); i++ ) {
			ret += skills.get(i) + "\n";
		} 

		ret += "\nSaving Throws\n";		
		ret += "physical: " + physical + "\n";
		ret += "mental: " + mental + "\n";
		ret += "evade: " + evade + "\n";
		ret += "luck: " + luck + "\n";
		ret += "tech: " + tech + "\n";
		
		ret += "\n";
		
		return ret;
	}

	public boolean hasLowAttribute () {
		return ( str < 8 ||dex < 8 ||con < 8 ||Int < 8 ||wis < 8 ||cha < 8 );
	}
	
	public boolean hasHighAttribute () {
		return ( str > 13 ||dex > 13 ||con > 13 ||Int > 13 ||wis > 13 ||cha > 13 );
	}

	// For finding skill in skills ArrayList
	public int getSkillIndex ( String input ) {
		for ( int i = 0; i < skills.size(); i ++ ) {
			if ( input.equals( skills.get(i).getName() ) ) {
				return i;
			}
		}
		return -1;
	}
	
	public int getAttIndex ( String input ) {
		int ret = -1;
		if ( input.equals("str") ) {
			ret = 0;
		} else if ( input.equals("dex") ) {
			ret = 1;
		} else if ( input.equals("con") ) {
			ret = 2;
		} else if ( input.equals("int") ) {
			ret = 3;
		} else if ( input.equals("wis") ) {
			ret = 4;
		} else if ( input.equals("cha") ) {
			ret = 5;
		} 	
		return ret;
	}

	// Used for leveling up a skill choosing from a category if needed
	public void addSkill ( String skill ) {
			
		Scanner scanner = new Scanner(System.in);
		String input;
			
		if ( skill.equals("choice") ) {
			System.out.println("Choose any skill");
			input = scanner.nextLine();
			while ( getSkillIndex( input ) < 0 || skillsAdded.contains( input ) ) {
				System.out.println("Error. Invalid skill. Must match skill exactly. Skills can only be chosen once.");
				input = scanner.nextLine();	
			}
			skills.get( getSkillIndex( input ) ).levelUp();
			skillsAdded.add( input );
		} else if ( skill.equals("combat/any") ) {
			System.out.println("Choose combat skill ");
			input = scanner.nextLine();
			while ( getSkillIndex( input ) < 0 || !input.startsWith("combat/") || skillsAdded.contains( input ) ) {
				System.out.println("Error. Invalid skill. Must match skill exactly. Skills can only be chosen once.");
				input = scanner.nextLine();	
			}
			skills.get( getSkillIndex( input ) ).levelUp();
			skillsAdded.add( input );
		} else if ( skill.equals("culture/any") ) {
			System.out.println("Choose culture skill ");
			input = scanner.nextLine();
			while ( getSkillIndex( input ) < 0 || !input.startsWith("culture/") || skillsAdded.contains( input ) ) {
				System.out.println("Error. Invalid skill. Must match skill exactly. Skills can only be chosen once.");
				input = scanner.nextLine();	
			}
			skills.get( getSkillIndex( input ) ).levelUp();
			skillsAdded.add( input );
		} else if ( skill.equals("vehicle/any") ) {
			System.out.println("Choose vehicle skill ");
			input = scanner.nextLine();
			while ( getSkillIndex( input ) < 0 || !input.startsWith("vehicle/") || skillsAdded.contains( input ) ) {
				System.out.println("Error. Invalid skill. Must match skill exactly. Skills can only be chosen once.");
				input = scanner.nextLine();	
			}
			skills.get( getSkillIndex( input ) ).levelUp();
			skillsAdded.add( input );
		} else if ( skill.equals("tech/any") ) {
			System.out.println("Choose tech skill ");
			input = scanner.nextLine();
			while ( getSkillIndex( input ) < 0 || !input.startsWith("tech/") || skillsAdded.contains( input ) ) {
				System.out.println("Error. Invalid skill. Must match skill exactly. Skills can only be chosen once.");
				input = scanner.nextLine();	
			}
			skills.get( getSkillIndex( input ) ).levelUp();
			skillsAdded.add( input );
		} else if ( getSkillIndex( skill ) >= 0 ) {
			skills.get( getSkillIndex(skill) ).levelUp();
			skillsAdded.add( skill );
		} else {
			System.out.println( "Error adding skill: " + skill );
		}
		
	}

	// Used for satisfying requirements where the player must pick two of a list of skills
	public void pick2skills ( String list ) {
		Scanner scanner = new Scanner(System.in);
		String input;
		String [] tmp = list.split(";");
		ArrayList<String> choices = new ArrayList<String>();
		
		System.out.println("Choose two skills from the following");
		for ( int i = 0; i < tmp.length; i++ ) {
			choices.add( tmp[i] );
			System.out.print( tmp[i] );
			if ( i < tmp.length - 1 ) {
				System.out.print(", ");
			} else {
				System.out.print("\n");
			}
		}
		
		for ( int i = 0; i < 2; i++ ) {
		input = scanner.nextLine();
			while ( !choices.contains( input ) ) {
				System.out.println("Error. Input must match a trait.");
				input = scanner.nextLine(); 
			}
			addSkill( input );
			choices.remove( input );
		}
		
	}

	// Taking the array of starting equipment items and adds the corresponding number choice. 
	public void addStartEquip( ArrayList<String []> startingGear, int choice ) {
		for ( int i = 0; i < startingGear.size(); i++ ) {
			if ( choice >= Integer.parseInt( startingGear.get(i)[0] ) && choice <= Integer.parseInt( startingGear.get(i)[1] ) ) {
				equip.add(new Equipment( startingGear.get(i)[2].trim(),  Double.parseDouble(startingGear.get(i)[4]), Integer.parseInt(startingGear.get(i)[3]) ) );
				System.out.println( "\nAdded: " + startingGear.get(i)[2] );
				break;
			}
		}
		
		
		
	}

	// Reads a shosen class file and allows a player to choose skills
	public void choosePackage( String Class ) {
		Scanner scanner = new Scanner(System.in);
		String input;
		skillsAdded = new ArrayList<String>();
		
		try (BufferedReader br = new BufferedReader(new FileReader( Class + ".txt"))) {
			String line;
			// Read base attack
			line = br.readLine();
			ab = Integer.parseInt(line);
			// Read in saving throws
			line = br.readLine();
			physical = Integer.parseInt(line);
			line = br.readLine();
			mental = Integer.parseInt(line);
			line = br.readLine();
			evade = Integer.parseInt(line);
			line = br.readLine();
			luck = Integer.parseInt(line);
			line = br.readLine();
			tech = Integer.parseInt(line);
			
			// Hold package and skills
			String[] packageData;
			ArrayList<String> packages = new ArrayList<String> ();
			ArrayList<String> packageSkills = new ArrayList<String> ();
			
			System.out.println("\nChoose trainging package\n");
			
			while ((line = br.readLine()) != null) {
			  System.out.println( line );
			  packageData = line.split(":");
			  packages.add( packageData[0] );
			  //System.out.println( "Added: " + packageData[0]);
			  packageSkills.add( packageData[1] );
			}
			


			
			input = scanner.nextLine();
			while ( !packages.contains( input ) ) {
				System.out.println("Error. Input must match a training package.");
				input = scanner.nextLine(); 
			} 
			training = input;
			
			for ( int i = 0; i < packages.size(); i ++ ) {
				if ( input.equals( packages.get(i) ) ) {
						packageData = packageSkills.get(i).split(",");
						for( int j = 0; j < packageData.length; j++ ) {
							if ( packageData[j].trim().startsWith("(") ) {
								pick2skills( packageData[j].trim().substring( 1, packageData[j].trim().length() -1 ) );
							} else {
								addSkill( packageData[j].trim() );
							}
						}
					break;
				} // End skill adding loop
			} // End package decision loop
		
			
		} catch ( Exception E ) {
			System.out.println("Error reading class file\n");
		}

	}

	public void printAttributes () {
		System.out.println("Str: " + str + "\t" + getMod(str) + "\n");
		System.out.println("Dex: " + dex + "\t" + getMod(dex) + "\n");
		System.out.println("Con: " + con + "\t" + getMod(con) + "\n");
		System.out.println("Int: " + Int + "\t" + getMod(Int) + "\n");
		System.out.println("Wis: " + wis + "\t" + getMod(wis) + "\n");
		System.out.println("Cha: " + cha + "\t" + getMod(cha) + "\n");
	}


	// Proceedure to generate an Aftermath character
	public void charGen () {
		Scanner scanner = new Scanner(System.in);
		String input;
		
		
		// Roll and modify attributes
		System.out.println("Roll Attributes");
		printAttributes();
		if ( hasLowAttribute() && hasHighAttribute () ) { 
			System.out.println("Move points if needed or type 'next'");
			System.out.println("to move type larger>smaller ex: dex>con");
			input = scanner.nextLine();
			
			String[] toMove;
			int [] attributes = { str, dex, con, Int, wis, cha };
			int srcInd, destInd;
			
			while ( hasLowAttribute() && hasHighAttribute () && !input.equals("next") ) {
				
				toMove = input.split(">");
				if ( toMove.length == 2 ) {
					srcInd = getAttIndex( toMove[0] );
					destInd = getAttIndex( toMove[1] );
					if ( srcInd < 0 || destInd < 0 ) {
						System.out.println("Error. Invalid attribute. Use lowercase?");
					} else {
						if ( attributes[srcInd] > 13 && attributes[destInd] < 8 && destInd != srcInd ) {
							attributes[srcInd]--;
							attributes[destInd]++;
							str = attributes[0];
							dex = attributes[1];
							con = attributes[2];
							Int = attributes[3];
							wis = attributes[4];
							cha = attributes[5];
							printAttributes();
							System.out.println();
						} else {
							System.out.println("Error. Cannot be lowered below 13 or raised above 8." );
						}
					}
					
				} else {
					System.out.println("Error. Shitty input");
				}
				if ( hasLowAttribute() && hasHighAttribute () && !input.equals("next") ) {
					input = scanner.nextLine();
				}
			} //End while
		} //End attribute adjustment


		// Load and choose backgrounds and skills if needed
		System.out.println("\nChoose background");
		skillsAdded = new ArrayList<String>();		
		String[] backgroundData;
		ArrayList<String> backgrounds = new ArrayList<String> ();
		ArrayList<String> backgroundSkills = new ArrayList<String> ();
		try (BufferedReader br = new BufferedReader(new FileReader("backgrounds.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
			  System.out.println( line );
			  backgroundData = line.split(":");
			  backgrounds.add( backgroundData[0] );
			  backgroundSkills.add( backgroundData[1] );
			}
		} catch ( Exception E ) {
			System.out.println("Error reading backgrounds list\n");
			E.printStackTrace();
		}
		input = scanner.nextLine();
		while ( !backgrounds.contains( input ) ) {
			System.out.println("Error. Input must match background.");
			input = scanner.nextLine(); 
		}
		
		background = input;
		for ( int i = 0; i < backgrounds.size(); i ++ ) {
			if ( input.equals( backgrounds.get(i) ) ) {
				backgroundData = backgroundSkills.get(i).split(",");
				for( int j = 0; j < backgroundData.length; j++ ) {
					addSkill( backgroundData[j].trim() );
				}
				break;
			} // End skill adding loop
		} // End background decision loop
		
		
		// Choose class
		System.out.println("\nChoose class");
		System.out.println("\nscrounger, slayer, speaker, survivor");
		input = scanner.nextLine();
		while ( !input.equals("scrounger") && !input.equals("slayer") && !input.equals("speaker") && !input.equals("survivor") ) {
			System.out.println("Error. Input must match a class.");
			input = scanner.nextLine(); 
		}

		Class = input;
		choosePackage( Class );
		
		
		// Choose trait
		System.out.println("\nChoose trait\n");
		ArrayList<String> traits = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader("traits.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
			  System.out.println( line );
			  traits.add( line );
			}
		} catch ( Exception E ) {
			System.out.println("Error reading traits list\n");
			E.printStackTrace();
		}
		input = scanner.nextLine();
		while ( !traits.contains( input ) ) {
			System.out.println("Error. Input must match a trait.");
			input = scanner.nextLine(); 
		} 
		trait = input;
		
		// Roll HP
		if ( Class.equals("survivor") ) {
			maxhp = getMod( con ) + 2 + roll( 1,6 );
			curhp = maxhp;
		} else {
			maxhp = getMod( con ) + roll( 1,6 );
			if ( maxhp < 1 ) {
				maxhp = 1;
			}
			curhp = maxhp;
		}
		
		
		// Choose starting equipment
		ArrayList<String []> startingGear = new ArrayList<String []> ();
		String [] tmp;
		System.out.println( "Roll or choose starting equipment" );
		System.out.println( "Roll\tStarting Gear\t\t\t\tAmt\tEnc");
		try (BufferedReader br = new BufferedReader(new FileReader("gear.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				tmp = line.split(",");
				startingGear.add( tmp );
				System.out.println( tmp[0] + "-" + tmp[1] + "\t" + tmp[2] + "\t" + tmp[3] + "\t" + tmp[4]);
			}
		} catch ( Exception E ) {
			System.out.println("Error reading starting gear list\n");
			E.printStackTrace();
		}
		System.out.println( "\ntype roll to get two random or a number to pick one");
		for ( int i = 0; i < 3; i++ ) {
			input = scanner.nextLine();
			try {
				while ( !input.equals("roll") && ( Integer.parseInt( input ) < 0 || Integer.parseInt( input ) > 100 ) ) {
					System.out.println("Error. Input must match a class.");
					input = scanner.nextLine(); 
				}
			} catch ( java.lang.NumberFormatException E ) {
				System.out.println("Error. Invalid input. Rolling");
				input = "roll";
			}
			
			if ( input.equals( "roll" ) ) {
				addStartEquip( startingGear, roll(1,100) );
				addStartEquip( startingGear, roll(1,100) );
			} else {
				addStartEquip( startingGear, Integer.parseInt( input ) );
			} 
		}


		// Choose starting weapon
		ArrayList<String []> startingWeapons = new ArrayList<String []> ();
		ArrayList<String> weaponNames = new ArrayList<String>();
		System.out.println( "\nChoose starting weapon" );
		System.out.println( "Weapon\t\t\tAmt\tEnc\tdmg\trange");
		try (BufferedReader br = new BufferedReader(new FileReader("weapons.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				tmp = line.split(",");
				startingWeapons.add( tmp );
				weaponNames.add( tmp[0].trim() );
				System.out.println( tmp[0] + "\t" + tmp[1] + "\t" + tmp[2] + "\t" + tmp[4] + "\t" + tmp[5]);
			}
		} catch ( Exception E ) {
			System.out.println("Error reading starting gear list\n");
			E.printStackTrace();
		}
		
		input = scanner.nextLine(); 
		while ( !weaponNames.contains( input ) ) {
			System.out.println("Error. Input must match a weapon.");
			input = scanner.nextLine(); 
		}
		tmp =  startingWeapons.get( weaponNames.indexOf( input ) );
		equip.add( new Equipment ( tmp[0].trim(), Double.parseDouble( tmp[1].trim() ), Integer.parseInt( tmp[2].trim() ), Integer.parseInt( tmp[3].trim() ), tmp[4].trim(), tmp[5].trim(), Integer.parseInt( tmp[6].trim() ) ) );
		
		
		// Choose starting armor
		ArrayList<String []> startingArmors = new ArrayList<String []> ();
		ArrayList<String> armorNames = new ArrayList<String>();
		System.out.println( "\nChoose starting armor" );
		System.out.println( "Armor\t\t\tEnc\tAC");
		try (BufferedReader br = new BufferedReader(new FileReader("armors.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				tmp = line.split(",");
				startingArmors.add( tmp );
				armorNames.add( tmp[0].trim() );
				System.out.println( tmp[0] + "\t" + tmp[1] + "\t" + tmp[3] );
			}
		} catch ( Exception E ) {
			System.out.println("Error reading starting gear list\n");
			E.printStackTrace();
		}
		
		input = scanner.nextLine(); 
		while ( !armorNames.contains( input ) ) {
			System.out.println("Error. Input must match a armor.");
			input = scanner.nextLine(); 
		}
		tmp =  startingArmors.get( armorNames.indexOf( input ) );
		equip.add( new Equipment ( tmp[0].trim(), Double.parseDouble( tmp[1].trim() ), Integer.parseInt( tmp[2].trim() ), Integer.parseInt( tmp[3].trim() ) ) );
		
		ac = getTotalAC();
		
		// Set name
		System.out.println("\nInput name");
		input = scanner.nextLine(); 
		name = input;
		System.out.println("\nDONE!\n\n");
	}

	// Run main to generate a print an Aftermath character.
	public static void main(String[] args) {
		Character test = new Character();
		test.charGen();
		System.out.print( test );
	}
}
