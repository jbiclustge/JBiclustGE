package jbiclustge.utils.osystem.progcheck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.swingutils.progress.GeneralProcessProgressionChecker;

public class RInstallerProgressionChecker extends GeneralProcessProgressionChecker implements RPackageInstallInfoProgress{


	protected ArrayList<String> checkerrors=new ArrayList<>(Arrays.asList("had non-zero exit status"));
	protected ArrayList<String> cachederrorlines=new ArrayList<>();
	protected ArrayList<String> packagesfailedinstall=new ArrayList<>();
	protected boolean errorsfound=false;
	
	public static String RINSTALLERPROGRESSCHECK="RINSTALLERPROGRESSCHECK";
	public static String RPACKAGESFAILED="RPACKAGESFAILED";

	private RInstallerProgressionChecker(ArrayList<String> checkerrors) {
		this.checkerrors=new ArrayList<>(checkerrors);
	}
	

	public RInstallerProgressionChecker() {}
	
	public RInstallerProgressionChecker(InputStream instream) throws IOException {
		super(instream);
	}

	public RInstallerProgressionChecker checkForErrors(String...errors) {
		for (String error : errors) {
			checkerrors.add(error);
		}
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
					LogMessageCenter.getLogger().addErrorMessage(currentline);
				}
				else
					LogMessageCenter.getLogger().addInfoMessage(currentline);

			}

		}

		return true;
	}

	protected boolean checkErrors(String line) {

		for (String errorcheck : checkerrors) {
			if(line.contains(errorcheck))
				return errorsfound=true;
		}
		return false;
	}
	
	protected String getPackageName(String currline) {
		Pattern pat=Pattern.compile("installation of package\\s+(.*)\\s+had non-zero exit status");
		String name=null;
		
		Matcher m=pat.matcher(currline);
		if(m.find())
			name=m.group(1);
		

		if(name.startsWith("‘"))
			name=name.substring(1, name.length());
		if(name.endsWith("’"))
			name=name.substring(0, (name.length()-1));
		
		
		return name;
	}
	
	@Override
	public boolean errorsFound() {
		return errorsfound;
	}

	@Override
	public ArrayList<String> getCachedErrorLines() {
		return cachederrorlines;
	}

	@Override
	public ArrayList<String> getPackagesFailedToInstall() {
		return packagesfailedinstall;
	}

	@Override
	public void reset() {
		cachederrorlines=new ArrayList<>();
		packagesfailedinstall=new ArrayList<>();
		errorsfound=false;
	}

	/*@Override
	public RInstallerProgressionChecker copyWorkInstance() {
		return new RInstallerProgressionChecker(this.checkerrors);
	}*/
	
	@Override
	public RInstallerProgressionChecker copyInstance() {
		RInstallerProgressionChecker copy =new RInstallerProgressionChecker(this.checkerrors);
		copy.reset();
		return copy;
	}
	


}
