package cn.sd.core.util.filemonitor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 文件变化监视类
 * 使用：
 * 	public class QueriesFileListener implements FileChangeListener {
 *     public void fileChanged(String filename){ logger.info(filename + "改变了"); }
 *  }
 * 	FileMonitor.getInstance().addFileChangeListener(new QueriesFileListener(),"c:\1.txt", 1000);
 * 
 * @author rex
 * @version $Id: FileMonitor.java,v 1.1 2008/07/21 08:31:38 rex Exp $
 */
public class FileMonitor
{
	
	private static final Log logger = LogFactory.getLog(FileMonitor.class);
	private static final FileMonitor instance = new FileMonitor();
	private Timer timer;
	private Map<String,FileMonitorTask> timerEntries;
	
	private FileMonitor() {
		this.timerEntries = new HashMap<String,FileMonitorTask>();
		this.timer = new Timer(true);
	}
	
	public static FileMonitor getInstance() {
		return instance;
	}
	
	/**
	 * Add a file to the monitor
	 * 
	 * @param listener The file listener
	 * @param filename The filename to watch
	 * @param period 扫瞄间隔.
	 */
	public void addFileChangeListener(FileChangeListener listener, 
		String filename, long period) {
		this.removeFileChangeListener(filename);
		
		logger.info("Watching " + filename);
		
		FileMonitorTask task = new FileMonitorTask(listener, filename);
		
		this.timerEntries.put(filename, task);
		this.timer.schedule(task, period, period);
	}
	
	/**
	 * Stop watching a file
	 * 
	 * @param listener The file listener
	 * @param filename The filename to keep watch
	 */
	public void removeFileChangeListener(String filename) {
		FileMonitorTask task = (FileMonitorTask)this.timerEntries.remove(filename);
		if (task != null) {
			task.cancel();
		}
	}
	
	public void removeFileChangeListener() {
		for (java.util.Iterator it = this.timerEntries.keySet().iterator(); it.hasNext();) {
			String key=(String)it.next();
			it.remove();
			FileMonitorTask task = (FileMonitorTask)this.timerEntries.remove(key);
			if (task != null) {
				task.cancel();
			}
		}
	}
	
	private class FileMonitorTask extends TimerTask {
		private FileChangeListener listener;
		private String filename;
		private File monitoredFile;
		private long lastModified;
		
		public FileMonitorTask(FileChangeListener listener, String filename) {
			this.listener = listener;
			this.filename = filename;
			
			this.monitoredFile = new File(filename);
			if (!this.monitoredFile.exists()) {
				return;
			}
			
			this.lastModified = this.monitoredFile.lastModified();
		}
		
		public void run() {
			long latestChange = this.monitoredFile.lastModified();
			if (this.lastModified != latestChange) {
				this.lastModified = latestChange;
				
				this.listener.fileChanged(this.filename);
			}
		}
	}
}
