package amp.esp;

import java.util.ArrayList;

import com.google.common.collect.Lists;

public class EventMatcher {

    private boolean epl;
    private String env;
    private String stmtStart;
    private String preSep;
    private String inSep;

    private ArrayList<String> conds = Lists.newArrayList();
    private EventMatcher followed;

    private EventMatcher(boolean epl, String env, String pattern) {
        super();
        this.epl = epl;
        this.env = env;
        this.stmtStart = pattern;
        this.preSep = (epl) ? " where " : "";
        this.inSep = " and ";
    }

    public boolean isEPL() {
        return epl;
    }

    public String getPattern() {
        String pattern = String.format(stmtStart, getCondString());
        if (followed != null) {
            pattern = String.format("%s -> %s", pattern, followed.getPattern());
        }
        return pattern;
    }

    private String getCondString() {
        StringBuffer sb = new StringBuffer();
        String sep = preSep;
        for (String cond : conds) {
            sb.append(sep);
            sb.append(cond);
            sep = inSep;
        }
        return sb.toString();
    }

    // ******************************************************************

    public static EventMatcher selectEnvelope(String ref) {
        return new EventMatcher(true, ref, doSelect(ref, "Envelope") + "%s");
    }

    public static EventMatcher selectInferredEvent(String ref) {
        return new EventMatcher(true, ref, doSelect(ref, "InferredEvent") + "%s");
    }

    public static String doSelect(String ref, String type) {
        return "select " + ref + " from " + type + " as " + ref;
    }

    public static EventMatcher everyEnvelope(String envref) {
        return new EventMatcher(false, envref, "every " + envref + "=Envelope(%s)");
    }

    public static EventMatcher matcher(boolean epl, String pattern) {
        return new EventMatcher(epl, null, pattern);
    }

    private static String quote(String str) { return "\"" + str + "\""; }

//    public static String checkHeader(String envref, String header, String headerval) {
//        return envref + "." + matching(header, headerval);
//    }

    public EventMatcher matching(String header, String headerval) {
        return fieldmatch(header, quote(headerval));
    }

    private EventMatcher fieldmatch(String header, String val) {
        conds.add(String.format("%s = %s", referenceValue(env, header), val));
        return this;
    }

    public static String referenceValue(String envel, String header) {
        return String.format("%s.getEnvelope().getHeader(%s)", envel, quote(header));
    }

    public EventMatcher matchingRef(String header, String env2, String header2) {
        return fieldmatch(header, referenceValue(env2, header2));
    }

    public EventMatcher followedBy(EventMatcher em2) {
        this.followed = em2;
        return this;
    }
}
