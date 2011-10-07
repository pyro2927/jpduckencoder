import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;

public class Encoder
{
	static boolean debug = false;
	public static void main(String[] args)
	{

		boolean rnFix = false;
		String exePayload = null;
		int chunksize = 256;

		String helpStr = "JPDuckEncoder v0.2, based off of Hak5 Duck Encoder 1.0\nAll original work done by Hak5, I've just added some slight modifications\n\n" + 
		"JPFixes:\n" +
		"v0.2\n" +
		"DEFAULT_DELAY now works correctly\n" +
		"v0.1\n" +
		"* Adding the flag --replacern will fix most issues users are having with carriage return\n" + 
		"* Fixed error where setting DEFAULTDELAY or DELAY would result in an \"Error on line\" message\n" + 
		"\nusage: duckencode -i [file ..]\t\t\tencode specified file\n   or: duckencode -i [file ..] -o [file ..]\tencode to specified file\n\nArguments:\n   -i [file ..] \t\tInput File\n   -o [file ..] \t\tOutput File\n\nScript Commands:\n   ALT [Char | END | (ESCAPE | ESC) | F1...F12 | SPACE  | TAB]\n   BREAK | PAUSE\n   CAPSLOCK\n   CONTROL | CTRL [(BREAK | PAUSE) | Char | F1...F12 | (ESCAPE | ESC)]\n   DEFAULT_DELAY | DEFAULTDELAY [Time in millisecond * 10]\n   DELAY [Time in millisecond * 10]\n   DELETE\n   DOWNARROW | DOWN\n   END\n   ESCAPE | ESC\n   F1...F12\n   HOME\n   INSERT\n   LEFTARROW | LEFT\n   MENU | APP\n   NUMLOCK\n   PAGEDOWN\n   PAGEUP\n   PRINTSCREEN\n   REM\n   RIGHTARROW | RIGHT\n   SCROLLLOCK\n   SHIFT [ DELETE | HOME | INSERT | PAGEUP | PAGEDOWN | (WINDOWS | GUI)\n         | (UPARROW | DOWNARROW |LEFTARROW | RIGHTARROW) | TAB]\n   SPACE\n   STRING [a...z A...Z 0..9 !...) `~ += _- \"' :; <, >. ?/ \\|]\n   TAB\n   UPARROW | UP\n   WINDOWS | GUI\n";

		String inputFile = null;
		String outputFile = null;

		//if no args are supplied, print the help text
		if ((args.length == 0)) {
			System.out.println(helpStr);
			System.exit(0);
		}

		for (int i = 0; i < args.length; i++) {
			//launch gui, unsupported at this time
			if ((args[i].equals("--gui")) || (args[i].equals("-g"))) {
				System.out.println("Launch GUI");
				//print the help text
			} else if ((args[i].equals("--help")) || (args[i].equals("-h"))) {
				System.out.println(helpStr);
				//set input file
			} else if (args[i].equals("-i"))
			{
				i++; inputFile = args[i];
				//set output file
			} else if (args[i].equals("-o"))
			{
				i++; outputFile = args[i];
			/**********************************
			 * Joe's additional arg handlers
			 **********************************/
			//remove CR of CRLF for Windows users mostly
			} else if(args[i].equals("--replacern")){ 
				rnFix = true;
			}/* else if(args[i].equals("--exepayload")){
				//gather our EXE payload
				i++; 
				exePayload = args[i];
			}*/
			
			else {
				System.out.println(helpStr);
				break;
			}
		}

		/*
		//if we have an exe payload, try to read it in encode it in a pre-canned payload to be typed up
		if(exePayload != null)
		{
			try {
				//first, try to open the file
				
				inputFile = "temp.txt";
				
				//setup our files
				File temp = new File(inputFile);
				File payload = new File(exePayload);
				byte[] buf = new byte[(int)payload.length()];
				DataInputStream in = new DataInputStream(new FileInputStream(payload));
				in.readFully(buf);
				String payloadString = new String(buf);
				
				//now that we have our payload string, try to dump it into a 
				//FileOutputStream outStream = new FileOutputStream(temp);
				FileOutputStream tempWriter = new FileOutputStream(temp);
				tempWriter.write(new String("DELAY 1000\n").getBytes());
				tempWriter.write(new String("GUI r\n").getBytes());
				tempWriter.write(new String("STRING notepad.exe\n").getBytes());
				tempWriter.write(new String("ENTER\n").getBytes());
				
				//now string out our hex values
				int offset = 0;
				//write our buffer to this text file
				while(offset < buf.length){
					//write some string
					//make sure we don't exceed our string length
					//tempWriter.write("STRING " + payloadString.substring(offset, (chunksize + offset < payloadString.length() - 1 ? chunksize + offset : payloadString.length() - 1) ) + "\n");
					tempWriter.write(new String("STRING ").getBytes());
					tempWriter.write(new String(buf, offset, (chunksize + offset < buf.length - 1 ? chunksize : buf.length - ( 1 + chunksize + offset )) ).getBytes() );
					tempWriter.write(new String("\n").getBytes());
					offset += chunksize;
				}
				
				//now hotkey to save the document
				tempWriter.write(new String("CTRL s\n").getBytes());
				
				tempWriter.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		
		if (inputFile != null) {
			String scriptStr = null;

			//read an RTF
			if (inputFile.contains(".rtf")) {
				try {
					FileInputStream stream = new FileInputStream(inputFile);
					RTFEditorKit kit = new RTFEditorKit();
					Document doc = kit.createDefaultDocument();
					kit.read(stream, doc, 0);

					scriptStr = doc.getText(0, doc.getLength());
				} catch (IOException e) {
					System.out.println("Error with input file!");
				} catch (BadLocationException e) {
					System.out.println("Error with input file!");
				}
			} else {
				//read a regular text file
				DataInputStream in = null;
				try {
					File f = new File(inputFile);
					byte[] buffer = new byte[(int)f.length()];
					in = new DataInputStream(new FileInputStream(f));
					in.readFully(buffer);
					scriptStr = new String(buffer);
				}
				catch (IOException e) {
					System.out.println("Error with input file!");
					try
					{
						in.close(); } catch (IOException localIOException1) {
						} } finally { try { in.close();
						} catch (IOException localIOException2)
						{
						}
						}
			}
			//check to see if we should implement any JP fixes
			if(rnFix){
				scriptStr = scriptStr.replaceAll("\r\n", "\n");
			}

			List<Byte> file = new ArrayList<Byte>();

			encodeToFile(scriptStr, outputFile == null ? "inject.bin" : 
				outputFile, file);

			//since we no pass in the file object, when we get it back, we need to handle savimg it outself
			//this will allow for other applications to use this class and handling the file storage
			//by themselves

			byte[] data = new byte[file.size()];
			for (int i = 0; i < file.size(); i++)
				data[i] = ((Byte)file.get(i)).byteValue();
			try
			{
				File someFile = new File(outputFile == null ? "inject.bin": outputFile);
				FileOutputStream fos = new FileOutputStream(someFile);
				fos.write(data);
				fos.flush();
				fos.close();
			} catch (Exception e) {
				System.out.print("Failed to write hex file!");
			}
		}
	}

	//modify encode to file to spit it back to a List<Byte> we pass in
	private static void encodeToFile(String inStr, String fileDest, List<Byte>file) {
		//split up each line as it's own instruction set
		String[] instructions = inStr.split("\n");
		int defaultDelay = 0;

		for (int i = 0; i < instructions.length; i++) {
			try {
				boolean delayOverride = false;
				String[] instruction = instructions[i].split(" ", 2);
				//if we have debug, print out our instructions
				if(debug && instruction.length > 1){
					System.out.println("Instructions: " + instruction[0] + ", " +instruction[1] );
				}
				//set a default delay
				if ((instruction[0].equals("DEFAULT_DELAY")) || 
						(instruction[0].equals("DEFAULTDELAY"))) {
					defaultDelay = Integer.valueOf(instruction[1]);
					//set a specific delay for this line
					//delay instructions start with a 0 byte,
					//then -1 byte for every 255 miliseconds
					//then the remainder byte value
				} else if (instruction[0].equals("DELAY")) {
					int delay = Integer.valueOf(instruction[1]);
					while (delay > 0) {
						file.add(Byte.valueOf((byte)0));
						if (delay > 255) {
							file.add(Byte.valueOf((byte)-1));
							delay -= 255;
						} else {
							file.add(Byte.valueOf((byte)delay));
							delay = 0;
						}
					}
					delayOverride = true;
					//print a string
				} else if (instruction[0].equals("STRING")) {
					//print out each character, instruction[1] holds the entire string
					for (int j = 0; j < instruction[1].length(); j++) {
						char c = instruction[1].charAt(j);
						file.add(Byte.valueOf((byte)charToByte(c)));

						//shift byte tells us to press the shift key
						byte shiftByte = 0;
						if ((c >= 'A') && (c <= 'Z'))
						{
							shiftByte = 2;
						}
						//see if we have any characters we need to hold shift for
						else switch (c)
						{
						case '!':
						case '"':
						case '#':
						case '$':
						case '%':
						case '&':
						case '(':
						case ')':
						case '*':
						case '+':
						case ':':
						case '<':
						case '>':
						case '?':
						case '@':
						case '^':
						case '_':
						case '{':
						case '|':
						case '}':
						case '~':
							shiftByte = 2;
						}


						file.add(Byte.valueOf((byte)shiftByte));
					}
				} else if ((instruction[0].equals("CONTROL")) || (instruction[0].equals("CTRL"))) {
					//check to see if we have any other keys to go with this
					if ((instruction[1].equals("ESCAPE")) || 
							(instruction[1].equals("ESC")))
						file.add(Byte.valueOf((byte)41));
					else if ((instruction[1].equals("PAUSE")) || 
							(instruction[1].equals("BREAK")))
						file.add(Byte.valueOf((byte)72));
					//see if we actually even have a second instruciton
					else if (instruction.length != 1) {
						if (functionKeyCheck(instruction[1]))
							file.add(Byte.valueOf((byte)functionKeyToByte(instruction[1])));
						else
							file.add(Byte.valueOf((byte)charToByte(instruction[1].charAt(0))));
					}
					else
					{
						if(debug){
							System.out.println("CTRL with no modifiers");
						}
						file.add(Byte.valueOf((byte)0));
					}
					file.add(Byte.valueOf((byte)1));
					//ALT+ combos
				} else if (instruction[0].equals("ALT")) {
					if (instruction.length != 1) {
						if ((instruction[1].equals("ESCAPE")) || (instruction[1].equals("ESC")))
							file.add(Byte.valueOf((byte)41));
						else if (instruction[1].equals("SPACE"))
							file.add(Byte.valueOf((byte)44));
						else if (instruction[1].equals("TAB"))
							file.add(Byte.valueOf((byte)43));
						else if (instruction.length != 1) {
							if (functionKeyCheck(instruction[1]))
								file.add(Byte.valueOf((byte)functionKeyToByte(instruction[1])));
							else
								file.add(Byte.valueOf((byte)charToByte(instruction[1].charAt(0))));
						}
						else
						{
							if(debug){
								System.out.println("CTRL with no modifiers");
							}
							file.add(Byte.valueOf((byte)0));
						}
					}
					//if we only have ALT, pad it with a 0
					else {
						file.add(Byte.valueOf((byte)0));
					}
					file.add(Byte.valueOf((byte)-30));
				}
				//enter key
				else if (instruction[0].equals("ENTER")) {
					file.add(Byte.valueOf((byte)40));
					file.add(Byte.valueOf((byte)0));
					//shift key
				} else if (instruction[0].equals("SHIFT")) {
					if (instruction.length != 1) {
						if (instruction[1].equals("HOME"))
							file.add(Byte.valueOf((byte)74));
						else if (instruction[1].equals("TAB"))
							file.add(Byte.valueOf((byte)43));
						else if ((instruction[1].equals("WINDOWS")) || 
								(instruction[1].equals("GUI")))
							file.add(Byte.valueOf((byte)-29));
						else if (instruction[1].equals("INSERT"))
							file.add(Byte.valueOf((byte)73));
						else if (instruction[1].equals("PAGEUP"))
							file.add(Byte.valueOf((byte)75));
						else if (instruction[1].equals("PAGEDOWN"))
							file.add(Byte.valueOf((byte)78));
						else if (instruction[1].equals("DELETE"))
							file.add(Byte.valueOf((byte)76));
						else if (instruction[1].equals("END"))
							file.add(Byte.valueOf((byte)77));
						else if (instruction[1].equals("UPARROW"))
							file.add(Byte.valueOf((byte)82));
						else if (instruction[1].equals("DOWNARROW"))
							file.add(Byte.valueOf((byte)81));
						else if (instruction[1].equals("LEFTARROW"))
							file.add(Byte.valueOf((byte)80));
						else if (instruction[1].equals("RIGHTARROW")) {
							file.add(Byte.valueOf((byte)79));
						}
						file.add(Byte.valueOf((byte)-31));
					} else {
						file.add(Byte.valueOf((byte)-31));
						file.add(Byte.valueOf((byte)0));
					}
				} 
				//this is where some custom joe commands come in
				else if(instruction[0].equals("CTLALTDEL")) {
					//control
					file.add(Byte.valueOf((byte)1));
					//alt
					file.add(Byte.valueOf((byte)-30));
					//delete
					file.add(Byte.valueOf((byte)76));

				} else {
					if (instruction[0].equals("REM"))
						continue;
					if ((instruction[0].equals("MENU")) || 
							(instruction[0].equals("APP"))) {
						file.add(Byte.valueOf((byte)101));
						file.add(Byte.valueOf((byte)0));
					} else if (instruction[0].equals("TAB")) {
						file.add(Byte.valueOf((byte)43));
						file.add(Byte.valueOf((byte)0));
					} else if (instruction[0].equals("SPACE")) {
						file.add(Byte.valueOf((byte)44));
						file.add(Byte.valueOf((byte)0));
					} else if ((instruction[0].equals("WINDOWS")) || 
							(instruction[0].equals("GUI"))) {
						if (instruction.length == 1) {
							file.add(Byte.valueOf((byte)-29));
							file.add(Byte.valueOf((byte)0));
						} else {
							file.add(Byte.valueOf((byte)charToByte(instruction[1].charAt(0))));
							file.add(Byte.valueOf((byte)8));
						}
					} else if (instruction[0].equals("SYSTEMPOWER")) {
						file.add(Byte.valueOf((byte)-127));
						file.add(Byte.valueOf((byte)0));
					} else if (instruction[0].equals("SYSTEMSLEEP")) {
						file.add(Byte.valueOf((byte)-126));
						file.add(Byte.valueOf((byte)0));
					} else if (instruction[0].equals("SYSTEMWAKE")) {
						file.add(Byte.valueOf((byte)-125));
						file.add(Byte.valueOf((byte)0));
					} else if ((instruction[0].equals("ESCAPE")) || 
							(instruction[0].equals("ESC"))) {
						file.add(Byte.valueOf((byte)41));
						file.add(Byte.valueOf((byte)0));
					} else if (instruction[0].equals("CAPSLOCK")) {
						file.add(Byte.valueOf((byte)57));
						file.add(Byte.valueOf((byte)0));
					} else if (instruction[0].equals("PRINTSCREEN")) {
						file.add(Byte.valueOf((byte)70));
						file.add(Byte.valueOf((byte)0));
					} else if (instruction[0].equals("SCROLLLOCK")) {
						file.add(Byte.valueOf((byte)71));
						file.add(Byte.valueOf((byte)0));
					} else if ((instruction[0].equals("BREAK")) || 
							(instruction[0].equals("PAUSE"))) {
						file.add(Byte.valueOf((byte)72));
						file.add(Byte.valueOf((byte)0));
					} else if (instruction[0].equals("INSERT")) {
						file.add(Byte.valueOf((byte)73));
						file.add(Byte.valueOf((byte)0));
					} else if (instruction[0].equals("HOME")) {
						file.add(Byte.valueOf((byte)74));
						file.add(Byte.valueOf((byte)0));
					} else if (instruction[0].equals("END")) {
						file.add(Byte.valueOf((byte)77));
						file.add(Byte.valueOf((byte)0));
					} else if (instruction[0].equals("PAGEUP")) {
						file.add(Byte.valueOf((byte)75));
						file.add(Byte.valueOf((byte)0));
					} else if (instruction[0].equals("DELETE")) {
						file.add(Byte.valueOf((byte)76));
						file.add(Byte.valueOf((byte)0));
					} else if (instruction[0].equals("PAGEDOWN")) {
						file.add(Byte.valueOf((byte)78));
						file.add(Byte.valueOf((byte)0));
					} else if ((instruction[0].equals("RIGHTARROW")) || 
							(instruction[0].equals("RIGHT"))) {
						file.add(Byte.valueOf((byte)79));
						file.add(Byte.valueOf((byte)0));
					} else if ((instruction[0].equals("LEFTARROW")) || 
							(instruction[0].equals("LEFT"))) {
						file.add(Byte.valueOf((byte)80));
						file.add(Byte.valueOf((byte)0));
					} else if ((instruction[0].equals("DOWNARROW")) || (instruction[0].equals("DOWN"))) {
						file.add(Byte.valueOf((byte)81));
						file.add(Byte.valueOf((byte)0));
					} else if ((instruction[0].equals("UPARROW")) || (instruction[0].equals("UP"))) {
						file.add(Byte.valueOf((byte)82));
						file.add(Byte.valueOf((byte)0));
					} else if (instruction[0].equals("NUMLOCK")) {
						file.add(Byte.valueOf((byte)83));
						file.add(Byte.valueOf((byte)0));
					} else if (instruction[0].equals("STOP")) {
						file.add(Byte.valueOf((byte)-75));
						file.add(Byte.valueOf((byte)0));
					} else if ((instruction[0].equals("PLAY")) || 
							(instruction[0].equals("PAUSE"))) {
						file.add(Byte.valueOf((byte)-51));
						file.add(Byte.valueOf((byte)0));
					} else if (instruction[0].equals("MUTE")) {
						file.add(Byte.valueOf((byte)-30));
						file.add(Byte.valueOf((byte)0));
					} else if (instruction[0].equals("VOLUMEUP")) {
						file.add(Byte.valueOf((byte)-23));
						file.add(Byte.valueOf((byte)0));
					} else if (instruction[0].equals("VOLUMEDOWN")) {
						file.add(Byte.valueOf((byte)-22));
						file.add(Byte.valueOf((byte)0));
					} else if (functionKeyCheck(instruction[0]))
					{
						file.add(Byte.valueOf((byte)functionKeyToByte(instruction[0])));
						file.add(Byte.valueOf((byte)0));
					}
				}

				//check to see if we have a default delay, or a single delay ovverride for this line
				//0 -1 0 -1 0 -1 0 10 is the styling for delays
				//start with 0, -1 for each 255, then remainder
				//a zero goes in front of every delay
				if (!delayOverride && defaultDelay > 0){
					if (debug) {
						System.out.println("Adding in default delay");
					}
					//copy the defaultDelay into a new integer so we can use it more than once
					int temp = new Integer(defaultDelay);
					while (temp > 0) {
						file.add(Byte.valueOf((byte)0));
						if (temp > 255) {
							file.add(Byte.valueOf((byte)-1));
							temp -= 255;
						} else {
							file.add(Byte.valueOf((byte)temp));
							temp = 0;
						}
					}
				}
			}
			//shit something fucked up, print an error message
			catch (Exception e) {
				System.out.println("Error on Line: " + (i + 1) + " : " + e.getLocalizedMessage());
			}

		}
	}

	//convert a character to a byte
	private static byte charToByte(char c)
	{
		if ((c >= 'a') && (c <= 'z'))
		{
			return (byte)(c - ']');
		}if ((c >= 'A') && (c <= 'Z'))
		{
			return (byte)(c - '=');
		}if ((c >= '1') && (c <= '9'))
		{
			return (byte)(c - '\023');
		}
		switch (c) {
		case ' ':
			return 44;
		case '!':
			return 30;
		case '@':
			return 31;
		case '#':
			return 32;
		case '$':
			return 33;
		case '%':
			return 34;
		case '^':
			return 35;
		case '&':
			return 36;
		case '*':
			return 37;
		case '(':
			return 38;
		case ')':
		case '0':
			return 39;
		case '-':
		case '_':
			return 45;
		case '+':
		case '=':
			return 46;
		case '[':
		case '{':
			return 47;
		case ']':
		case '}':
			return 48;
		case '\\':
		case '|':
			return 49;
		case ':':
		case ';':
			return 51;
		case '"':
		case '\'':
			return 52;
		case '`':
		case '~':
			return 53;
		case ',':
		case '<':
			return 54;
		case '.':
		case '>':
			return 55;
		case '/':
		case '?':
			return 56;
		}

		return -103;
	}

	//check to see if we have a function key
	private static boolean functionKeyCheck(String possibleFKey)
	{
		return (possibleFKey.equals("F1")) || (possibleFKey.equals("F2")) || 
		(possibleFKey.equals("F3")) || (possibleFKey.equals("F4")) || 
		(possibleFKey.equals("F5")) || (possibleFKey.equals("F6")) || 
		(possibleFKey.equals("F7")) || (possibleFKey.equals("F8")) || 
		(possibleFKey.equals("F9")) || (possibleFKey.equals("F10")) || 
		(possibleFKey.equals("F11")) || (possibleFKey.equals("F12"));
	}

	//convert a function key to a byte
	private static byte functionKeyToByte(String fKey)
	{
		if (fKey.equals("F1"))
			return 58;
		if (fKey.equals("F2"))
			return 59;
		if (fKey.equals("F3"))
			return 60;
		if (fKey.equals("F4"))
			return 61;
		if (fKey.equals("F5"))
			return 62;
		if (fKey.equals("F6"))
			return 63;
		if (fKey.equals("F7"))
			return 64;
		if (fKey.equals("F8"))
			return 65;
		if (fKey.equals("F9"))
			return 66;
		if (fKey.equals("F10"))
			return 67;
		if (fKey.equals("F11"))
			return 68;
		if (fKey.equals("F12")) {
			return 69;
		}
		return -103;
	}
}

/* Location:           /Users/joe/Downloads/duckencode.jar
 * Qualified Name:     Encoder
 * JD-Core Version:    0.6.0
 */