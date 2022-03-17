package org.uftwf.account.model;

import javax.naming.InitialContext;

/**
 * Created by xyang on 1/30/19.
 */
public class Apps {
   public static enum App{
        UNION,UNIONQUEUE,WELFARE,WELFAREQUEUE,GRIEVANCE,GRIEVANCEQUEUE,COS,COSQUEUE,UNIONQUIRY,WELFAREINQUIRY,CERTIFICATE,MOVIETICKET,NONMEMBERREPORT,CHAPTERSECTION,
       NURSESECTION,PEERSECTION,DISCOUTSECTION,JUSTFORFUNSECTION,UNIONCOS, COURSES,PAPERFORMREDUCTION,CONSULATIONCOMMITTEE,HELPDESKCONSOLE,OPTICAL,SALESFORCECONSELORCOMMUNITY
    }

    public static SuggestMemberApp getSuggestApp(App app){
        try {
            InitialContext ctx = new InitialContext();
            String host = (String) ctx.lookup("java:global/uft/host");
            String webhost= (String) ctx.lookup("java:global/uft/url");
            switch (app){
                case UNION:
                    return new SuggestMemberApp("Enroll in the UFT",host+"enrollment",1,"Gain access to valuable benefits and have a voice in your professional life.",true,false);
                case UNIONQUEUE:
                    return new SuggestMemberApp("Union Enrollment Admin Queue",host+"unionAdmin",3,"",true,false);
                case WELFARE:
                    return new SuggestMemberApp("Enroll in the UFT Welfare Fund",host+"welfare",2,"The union provides an array of benefits to supplement your health plan coverage. Welfare Fund enrollment is separate from your NYC health plan enrollment.",true,false);
                case WELFAREQUEUE:
                    return new SuggestMemberApp("Welfare Enrollment Admin Queue",host+"welfareAdmin",4,"",true,false);
                case GRIEVANCE:
                    return new SuggestMemberApp("File a Step 1 Grievance", host+"grievance",5,"Due to the COVID-19 pandemic, the online grievance process is suspended.  You are encouraged to attempt informal resolutions in the meantime. If you have any questions, please contact the UFT at 212-331-6311.",true,false);
                case GRIEVANCEQUEUE:
                    return new SuggestMemberApp("Grievance History",host+"grievance/admin",6,"See your school’s history of grievances",true,false);
                case COS:
                    return new SuggestMemberApp("Change of Status form",host+"cos/",7,"Update your name, address, marital status or family profile.",true,false);
                case COSQUEUE:
                    //need to be updated
                    return new SuggestMemberApp("Change of Status Admin Queue",host+"cosAdminQueue/",8,"",true,false);
                case UNIONQUIRY:
                    return new SuggestMemberApp("Ask the Union",host+"member-inquiry",8,"Submit a question to the UFT about your rights and benefits.",true,false);
                case WELFAREINQUIRY:
                    return new SuggestMemberApp("Ask the UFT Welfare Fund",
                            host+"member-inquiry-welfare",9,"Submit a question about your supplemental health benefits.",true,false);
                case CERTIFICATE:
                    return new SuggestMemberApp("Access Hearing Aid Benefits",host+"certificate",11,"Request Hearing Aid Certificate.",true,false);
                case OPTICAL:
                    return new SuggestMemberApp("Access Optical Benefits", host+"opticalCertificate",10,"Request Optical Services.",true,false);
                case MOVIETICKET:
                    return new SuggestMemberApp("Buy movie tickets",host+"movieTicket/",12,"Buy discounted movie tickets from the UFT.",true,false);
                case NONMEMBERREPORT:
                    return new SuggestMemberApp("Nonmember Report",host+"nonmember",10,"Find out which employees in the UFT bargaining unit in your school are not paying union dues.",true,false);
                case CHAPTERSECTION:
                    return new SuggestMemberApp("For Chapter Leaders Only", webhost+"chapters/chapter-leaders",13,"Access the UFT.org section for chapter leaders.",true,false);
                case NURSESECTION:
                    return new SuggestMemberApp("Read About UFT Benefits", webhost+"chapters/other-chapters/federation-nurses/federation-nurses-benefits/",14,"UFT Web Page for Visiting Nurse.",true,false);
                case PEERSECTION:
                    return new SuggestMemberApp("For PIP Staff Only", webhost+"your-union/uft-programs/peer-intervention-program/pip-staff/",15,"Access the UFT.org section for Peer Intervention Program staff.",true,false);
                case JUSTFORFUNSECTION:
                    return new SuggestMemberApp("Just For Fun", webhost+"your-benefits/discounts-and-promotions/just-fun/",16,"Find out about trips, defensive driving classes and other recreational activities offered by the UFT.",true,false);
                case DISCOUTSECTION:
                    return new SuggestMemberApp("Discount", webhost+"your-benefits/discounts-and-promotions/uft-discounts/",17,"See member-only discounts on apparel, entertainment, school supplies and more. ",true,false);
                case UNIONCOS:
                    return new SuggestMemberApp("Address Change",
                            host+"unioncoa",1,"Change Union Information.",true,false);
                case COURSES:
                    return new SuggestMemberApp("Enroll in a course",host+"courses/index?project=course",17,"Register for a UFT-sponsored course.",true,false);
                case SALESFORCECONSELORCOMMUNITY:
                    return new SuggestMemberApp("College and Career Counseling Program","https://training-uftnew.cs191.force.com/login?so=00D7c000008sDpZ",17,"See your appointment schedule.",true,true);
                case PAPERFORMREDUCTION:
                    return new SuggestMemberApp("File an operational issue report ",host+"forms/paperwork-reduction",18,"Use this form to report an issue at your school concerning blended learning, paperwork, curriculum, professional development, workload and other workplace issues.",true,false);
                case CONSULATIONCOMMITTEE:
                    return new SuggestMemberApp("File a consultation committee report",host+"forms/consultation-committee",19,"Use this form to record the issues discussed at the monthly meeting you hold with your consultation committee and your principal.",true,false);
                case HELPDESKCONSOLE:
                    return new SuggestMemberApp("Member Dashboard",host+"memberSearch/",20,"A tool for determining a member’s sign up, verification and mobile app eligibility status.",true,false);

            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
