package ms.aurora.transform;


import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.generic.*;
import org.apache.log4j.Logger;

/**
 * @author Rick
 */
public final class AccessorDefinition {
    private static final Logger logger = Logger.getLogger(AccessorDefinition.class);
    private String name;
    private String owner;
    private String field;
    private String signature;
    private String returnSignature;
    private boolean statik;
    private long multiplier;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getReturnSignature() {
        return returnSignature;
    }

    public void setReturnSignature(String returnSignature) {
        this.returnSignature = returnSignature;
    }

    public boolean isStatik() {
        return statik;
    }

    public void setStatik(boolean statik) {
        this.statik = statik;
    }

    public long getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(long multiplier) {
        this.multiplier = multiplier;
    }

    public void inject(ClassGen cg) {
        ConstantPoolGen cp = cg.getConstantPool();
        InstructionList instructions = new InstructionList();
        MethodGen getter = new MethodGen(Constants.ACC_PUBLIC,
                Type.getType(returnSignature), Type.NO_ARGS, new String[]{}, name,
                cg.getClassName(), instructions, cp);
        InstructionFactory factory = new InstructionFactory(cg, cp);
        instructions.append(new ALOAD(0));
        instructions.append(factory.createFieldAccess(owner, field,
                Type.getType(signature), statik ? Constants.GETSTATIC :
                Constants.GETFIELD));
        if (returnSignature.matches("(I|S|J|D)") && multiplier != 0) {
            int mIndex = cp.addConstant(new ConstantInteger((int) multiplier), cp);
            instructions.append(new LDC(mIndex));
            instructions.append(new IMUL());
        }
        instructions.append(InstructionFactory.createReturn(Type.getType(signature)));
        getter.setMaxStack();
        getter.setMaxLocals();
        cg.addMethod(getter.getMethod());
        logger.debug(toString());
    }

    @Override
    public String toString() {
        return new StringBuilder().append(name).append(" -> ").append(owner).
                append(".").append(field).append(multiplier != 0 ? " * " + multiplier : "").
                append(" [ ").append(signature).append("] => ").append(returnSignature).toString();
    }

}
