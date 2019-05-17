package jbiclustge.utils.osystem.progcheck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.beryx.textio.TextIO;
import org.beryx.textio.console.ConsoleTextTerminal;

import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;

public class RInstallerProgressionCheckerTextIO extends RInstallerProgressionChecker{
	
	protected TextIO textterminal;
	protected boolean showinfo=false;
	
	private RInstallerProgressionCheckerTextIO(TextIO textterminal,boolean showinfo, ArrayList<String> checkerrors) {
		this.checkerrors=new ArrayList<>(checkerrors);
		this.textterminal=textterminal;
		this.showinfo=showinfo;
	}
	
	
	public RInstallerProgressionCheckerTextIO(TextIO textterminal) {
		this.textterminal=textterminal;
	}
	
	public RInstallerProgressionCheckerTextIO(TextIO textterminal, InputStream instream) throws IOException {
		super(instream);
		this.textterminal=textterminal;
	}
	
	public RInstallerProgressionCheckerTextIO showInformation(boolean show) {
		this.showinfo=show;
		return this;
	}
	
	
	@Override
	protected Boolean doInBackground() throws Exception {

		if(stream!=null){

			BufferedReader inputFile = new BufferedReader(new InputStreamReader(stream));
			String currentline = null;
			
			while((currentline = inputFile.readLine()) != null) {
				
				if(checkErrors(currentline)) {
					errorsfound=true;
					cachederrorlines.add(currentline);
					String packagename=getPackageName(currentline);
					if(packagename!=null)
						packagesfailedinstall.add(packagename);
					if(showinfo && (textterminal.getTextTerminal() instanceof ConsoleTextTerminal)) {
						System.out.println("Error detected: "+currentline);
					}
					LogMessageCenter.getLogger().addErrorMessage(currentline);
				}
				else {
					if(showinfo && (textterminal.getTextTerminal() instanceof ConsoleTextTerminal))
							System.out.println(currentline);
		
					LogMessageCenter.getLogger().addInfoMessage(currentline);
				}

			}

		}

		return true;
	}
	
	
	@Override
	public RInstallerProgressionCheckerTextIO copyInstance() {
		RInstallerProgressionCheckerTextIO copy =new RInstallerProgressionCheckerTextIO(this.textterminal,showinfo,this.checkerrors);
		copy.reset();
		return copy;
	}

}
