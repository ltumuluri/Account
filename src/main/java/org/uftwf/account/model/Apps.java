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
                //WFAQ
                case UNIONQUEUE:
                    return new SuggestMemberApp("Union Enrollment Admin Queue",host+"unionAdmin",3,"",true,false);
                case WELFAREQUEUE:
                    return new SuggestMemberApp("Welfare Enrollment Admin Queue",host+"welfareAdmin",4,"",true,false);
                case COSQUEUE:
                    //need to be updated
                    return new SuggestMemberApp("Change of Status Admin Queue",host+"cosAdminQueue/",8,"",true,false);

                //Member
                case UNION:
                    return new SuggestMemberApp("Join the UFT",host+"enrollment",1,"Receive rights and benefits exclusive to UFT members.",true,false);
                case WELFARE:
                    return new SuggestMemberApp("Enroll in the UFT Welfare Fund",host+"welfare",2,"The union provides an array of benefits to supplement your health plan coverage. Welfare Fund enrollment is separate from your NYC health plan enrollment.",true,false);
                case COS:
                    return new SuggestMemberApp("Change of Status form",host+"cos/",7,"Update your name, address, marital status or family profile.",true,false);
                case UNIONCOS:
                    return new SuggestMemberApp("Change of Address",host+"unioncoa",8,"Change Union Information.",true,false);
                case UNIONQUIRY:
                    return new SuggestMemberApp("Ask the Union",host+"member-inquiry",8,"Submit a question or inquiry to UFT staff.",true,false);
                case WELFAREINQUIRY:
                    return new SuggestMemberApp("Ask the UFT Welfare Fund",host+"member-inquiry-welfare",9,"Submit a question about your supplemental health benefits.",true,false);
                case OPTICAL:
                    return new SuggestMemberApp("Access Optical Benefits", host+"opticalCertificate",10,"Request Optical Services.",true,false);
                case CERTIFICATE:
                    return new SuggestMemberApp("Access Hearing Aid Benefits",host+"certificate",11,"Request Hearing Aid Certificate.",true,false);
                case COURSES:
                    return new SuggestMemberApp("Enroll in a course",host+"courses/index?project=course",17,"View current UFT courses and register online.",true,false);
                case SALESFORCECONSELORCOMMUNITY:
                    return new SuggestMemberApp("College and Career Counseling Program","https://training-uftnew.cs191.force.com/login?so=00D7c000008sDpZ",17,"See your appointment schedule.",true,true);
                case DISCOUTSECTION:
                    return new SuggestMemberApp("Discount", webhost+"your-benefits/discounts-and-promotions/uft-discounts/",18,"See member-only discounts on apparel, entertainment, school supplies and more.",true,false);
                case MOVIETICKET:
                    return new SuggestMemberApp("Buy movie tickets",host+"movieTicket/",18,"Buy discounted movie tickets from the UFT.",true,false);


                //comment out
                case CHAPTERSECTION:
                    return new SuggestMemberApp("For Chapter Leaders Only", webhost+"chapters/chapter-leaders",13,"Access the UFT.org section for chapter leaders.",true,false);
                //comment out
                case NURSESECTION:
                    return new SuggestMemberApp("Read About UFT Benefits", webhost+"chapters/other-chapters/federation-nurses/federation-nurses-benefits/",14,"UFT Web Page for Visiting Nurse.",true,false);
                //comment out
                case PEERSECTION:
                    return new SuggestMemberApp("For PIP Staff Only", webhost+"your-union/uft-programs/peer-intervention-program/pip-staff/",15,"Access the UFT.org section for Peer Intervention Program staff.",true,false);
                //comment out
                case JUSTFORFUNSECTION:
                    return new SuggestMemberApp("Just For Fun", webhost+"your-benefits/discounts-and-promotions/just-fun/",16,"Find out about trips, defensive driving classes and other recreational activities offered by the UFT.",true,false);


                //CC
                case GRIEVANCE:
                    return new SuggestMemberApp("File a Step 1 Grievance", host+"grievance",5,"Submit grievances on behalf of your chapter members.",true,false);
                case GRIEVANCEQUEUE:
                    return new SuggestMemberApp("Grievance History",host+"grievance/admin",6,"View current and past grievances filed from your chapter.",true,false);
                case NONMEMBERREPORT:
                    return new SuggestMemberApp("Nonmember Report",host+"nonmember",10,"See which staff members at your school have not joined the union.",true,false);
                case PAPERFORMREDUCTION:
                    return new SuggestMemberApp("File an Operational Issue Report",host+"forms/paperwork-reduction",18,"Report an operational issue at your school to the UFT.",true,false);
                case CONSULATIONCOMMITTEE:
                    return new SuggestMemberApp("File a Consultation Committee Report",host+"forms/consultation-committee",19,"Record issues discussed during monthly meetings.",true,false);

                //MHD
                case HELPDESKCONSOLE:
                    return new SuggestMemberApp("Verify Personal Information",host+"memberSearch/",20,"View current records and preferences on file with the UFT",true,false);



            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
