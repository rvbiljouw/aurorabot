package ms.aurora.gui.script;

import ms.aurora.api.script.Script;
import ms.aurora.api.script.ScriptManifest;

@ScriptManifest(name = "Test script", author = "rvbiljouw", version = 1.0)
public class SampleScript extends Script {

	@Override
	public int tick() {
		return 1000;
	}

}
