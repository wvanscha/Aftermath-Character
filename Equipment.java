// Class to cover all types of equipment, booleans to identify type

public class Equipment {

	String name;
	int quantity;
	double enc;
	boolean isWeapon;
	boolean isArmor;
	int hit;
	String dmg;
	String range;
	int ammo;
	int ac;

	// General constructor for basic equipment.
	public Equipment ( String name, double enc, int quantity ) {
		this.name = name;
		this.enc = enc;
		this.quantity = quantity;
		this.isWeapon = false;
		this.isArmor = false;
		this.hit = 0;
		this.ammo = 0;
		this.ac = 0;	
		}
		
	// Armor constructor
	public Equipment ( String name, double enc, int quantity, int ac ) {
		this.name = name;
		this.enc = enc;
		this.quantity = quantity;
		this.isWeapon = false;
		this.isArmor = true;
		this.hit = 0;
		this.ammo = 0;
		this.ac = ac;	
		}
		
	// Weapon constructor
	public Equipment ( String name, double enc, int quantity, int hit, String dmg, String range, int ammo ) {
		this.name = name;
		this.enc = enc;
		this.quantity = quantity;
		this.isWeapon = true;
		this.isArmor = false;
		this.hit = hit;
		this.dmg = dmg;
		this.range = range;
		this.ammo = ammo;
		this.ac = 0;	
		}
		
	// Prints equipment as general equipment item
	public String toString () {
		if ( quantity > 1 ) {
			return ( quantity + " " + name + "\t\t" + enc );
		} else {
			return ( name + "\t" + enc );
		}
	}

	public String toArmor () {
		return ( name + "\t\t" + ac );
	}

	public String toWeapon () {
		return ( name + " \t" + hit + " \t" + dmg + " \t" + range + " \t" + ammo);
	}

	public String getName () {
		return this.name;
	}

	public boolean isArmor () {
		return this.isArmor;
	}

	public boolean isWeapon () {
		return this.isWeapon;
	}

	public double getEnc () {
		return this.enc;
	}

	public int getQuantity () {
		return this.quantity;
	}

	public int getHit () {
		return this.hit;
	}

	public int getAC () {
		return this.ac;
	}

	public void setHit ( int newHit ) {
		this.hit = newHit;
	}

	public void setQuantity ( int newQuantity ) {
		this.quantity = newQuantity;
	}

	public double totalEnc () {
		return ( quantity * enc );
	}

}
