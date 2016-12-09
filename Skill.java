// Class to hold a particular skill

public class Skill {

	String name;
	int bonus;
	int level;

	public Skill ( String name ) {
		this.name = name;
		this.bonus = -2;
		this.level = -1;
	}

	public String toString () {
		if ( level < 0 ) {
			return ( name );
		} else {
			return ( name + "\t\t" + level );
		}
	}

	public String getName () {
		return this.name;
	}
	
	public int getLevel () {
		return this.level;
	}

	public void levelUp () {
		if ( level == -1 ) {
			level++;
			bonus = 0;
		} else {
			level++;
			bonus++;
		}
	}

}
