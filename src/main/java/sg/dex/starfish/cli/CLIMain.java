package sg.dex.starfish.cli;

public class CLIMain {

	
	public static void main(String... args) {
		if (args.length==0) {
			System.out.println("Welcome to Starfish");
			System.exit(0);
		}
		
		String option=args[0];
		switch (option) {
			case "-getAsset": {
				System.out.println("TODO: get asset via DID");
		    }
			default: {
				System.err.println("Option not recognised: "+option);
			}
		}
	}
}
