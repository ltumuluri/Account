package org.uftwf.account.model;

import javax.naming.InitialContext;

public class NonMemberApps {
    public static enum NonMemberApp{
        VERIFY,UNION,WELFARE,CCP
    }
    public static NonMemberApplication getSuggestApp(NonMemberApps.NonMemberApp app){
        try {
            InitialContext ctx = new InitialContext();
            String host = (String) ctx.lookup("java:global/uft/host");
            switch (app){
                case VERIFY:
                    return new NonMemberApplication("Verify Your Identity",host+"verify",3,"Verify your identity for enhanced access to the UFT website and to access the UFT mobile app.");
                case CCP:
                    return new NonMemberApplication("Verify Child Care Provider",host+"verify/ccp",4,"If you are a licensed UFT family child care provider, please verify your identity.");
                case UNION:
                    return new NonMemberApplication("Enroll in the UFT",host+"enrollment",1,"Gain access to valuable benefits and have a voice in your professional life. ");
                case WELFARE:
                    return new NonMemberApplication("Enroll in the UFT Welfare Fund",host+"welfare",2,"The union provides an array of benefits to supplement your health plan coverage. Welfare Fund enrollment is separate from your NYC health plan enrollment.");
//                case COURSE:
//                    return new NonMemberApplication("Courses",host+"courses",5,"Courses Registration");
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
