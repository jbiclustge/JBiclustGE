package jbiclustge.utils.osystem.progcheck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.swingutils.progress.GeneralProcessProgressionChecker;

public class RInstallerProgressionChecker__back extends GeneralProcessProgressionChecker{
	
	
	
	
	
	public RInstallerProgressionChecker__back(InputStream instream) throws IOException {
		super(instream);
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		
		if(stream!=null){
			
			BufferedReader inputFile = new BufferedReader(new InputStreamReader(stream));
			String currentline = null;
	        while((currentline = inputFile.readLine()) != null) {
	        	    LogMessageCenter.getLogger().addInfoMessage(currentline);
	        	
	        }
			
		}
	
		return true;
	}

}
