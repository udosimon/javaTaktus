package AChord.AClasses;

import java.io.File;

public class AFile
{
	private File file;
	
	public AFile(File file) {
		super();
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String toString()
	{
		String st = file.getName();
		if (st.lastIndexOf(".taktus") > -1)
		{
			st = st.substring(0, st.lastIndexOf(".taktus"));
		}
		return st;
	}
	
}
