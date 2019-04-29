package sg.dex.starfish.cli;

/**
 * Main class for CLI invocations
 * 
 * @author Mike
 *
 */
public class CLIMain {

	/**
	 * Main entry point for CLI execution
	 * @param args Arguments from command line.
	 */
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
