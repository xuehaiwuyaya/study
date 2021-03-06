package plugin.template;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;

/**   
 * @Title: 步骤类 
 * @Package plugin.template 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author http://www.ahuoo.com  
 * @date 2010-8-8 下午05:10:26 
 * @version V1.0   
 */

public class TemplateStep extends BaseStep implements StepInterface {

	private TemplateStepData data;
	private TemplateStepMeta meta;
	
	public TemplateStep(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis) {
		super(s, stepDataInterface, c, t, dis);
	}

	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
		meta = (TemplateStepMeta) smi;
		data = (TemplateStepData) sdi;

		Object[] r = getRow(); // get row, blocks when needed!
		if (r == null) // no more input to be expected...
		{
			setOutputDone();
			return false;
		}

		if (first) {
			first = false;

			data.outputRowMeta = (RowMetaInterface) getInputRowMeta().clone();
			meta.getFields(data.outputRowMeta, getStepname(), null, null, this);

			logBasic("template step initialized successfully");

		}
		
		

		Object[] outputRow = RowDataUtil.addValueData(r, data.outputRowMeta.size() - 1, "dummy value");

		putRow(data.outputRowMeta, outputRow); // copy row to possible alternate rowset(s)

		if (checkFeedback(getLinesRead())) {
			logBasic("Linenr " + getLinesRead()); // Some basic logging
		}

		return true;
	}

	public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (TemplateStepMeta) smi;
		data = (TemplateStepData) sdi;

		return super.init(smi, sdi);
	}

	public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (TemplateStepMeta) smi;
		data = (TemplateStepData) sdi;

		super.dispose(smi, sdi);
	}

	//
	// Run is were the action happens!
	public void run() {
		logBasic("Starting to run...");
		try {
			while (processRow(meta, data) && !isStopped())
				;
		} catch (Exception e) {
			logError("Unexpected error : " + e.toString());
			logError(Const.getStackTracker(e));
			setErrors(1);
			stopAll();
		} finally {
			dispose(meta, data);
			logBasic("Finished, processing " + getLinesRead() + " rows");
			markStop();
		}
	}

}
