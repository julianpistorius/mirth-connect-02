package com.webreach.mirth.model.hl7v2.v23.segment;
import com.webreach.mirth.model.hl7v2.v23.composite.*;
import com.webreach.mirth.model.hl7v2.*;

public class _AIS extends Segment {
	public _AIS(){
		fields = new Class[]{_SI.class, _ID.class, _CE.class, _TS.class, _NM.class, _CE.class, _NM.class, _CE.class, _IS.class, _CE.class};
		repeats = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		required = new boolean[]{false, false, false, false, false, false, false, false, false, false};
		fieldDescriptions = new String[]{"Set ID", "Segment Action Code", "Universal Service ID", "Start Date/Time", "Start Date/Time Offset", "Start Date/Time Offset Units", "Duration", "Duration Units", "Allow Substitution Code", "Filler Status Code"};
		description = "Appointment Information - Service";
		name = "AIS";
	}
}
