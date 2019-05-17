package jbiclustge.utils.osystem.progcheck;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import pt.ornrocha.swingutils.progress.AbstractProcessProgressionChecker;

public interface RPackageInstallInfoProgress {
	
	
	public void setInputStream(InputStream instream) throws IOException;
	public ArrayList<String> getPackagesFailedToInstall();
	public boolean errorsFound();
	public ArrayList<String> getCachedErrorLines();
	public void reset();
	//public RPackageInstallInfoProgress copyWorkInstance();
}
