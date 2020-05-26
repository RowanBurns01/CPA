Welcome to my CPA Refactoring. 

Listed are the 7 issues I solved, the design pattern used, where in the code I have implemented it and any assumptions
 I have made.

    1. The Report class using excess ram was solved using the flyweight pattern.
        - This was implemented in the classes ReportImpl and ReportImplFlyweightFactory.
    
    2. The reduction of order class load was solved using the bridge pattern.
        - This was implemented in the interfaces Importance and Type and classes ImportanceCritical, ImportanceNormal,
         TypeAudit, TypeRegular, SingleOrderImpl and ScheduledOrderImpl.
        - Assumed that the 60+ other classes vary mostly by Type and so should be able to be independently created.
    
    3. The handling of client contact methods was streamlined using the chain of responsibility pattern.
        - This was implemented in the interface ContactChain and classes CarrierPigeonHandler, EmailHandler,
        InternalAccountingHandler, MailHandler, PhoneCallHandler, SMSHandler, and FEAAFacade.
               
    4. The loading lag of retrieving client data from the data base was mitigated using lazy loading.
        - This was implemented in the class ClientImpl.
        - Assumed that the the Client's data could expire at any time, so in order to avoid a validity issue I have 
        not cached Client data for subsequent data calls.
    
    5. The comparison of two report objects was streamlined by turning ReportImpl into a value object.
        - This was implemented in the class ReportImpl ( with updates to report comparing in SingleOrderImpl)
    
    6. The repeated slow database operations was mitigated by using the unit of work pattern.
        - This was implemented in the classes UnitOfWork and FEAAFacade.
        - Assumed the duration of a transaction was from log in to log out. However, commits also occur on order 
        finalising. This was chosen to ensure that an unexpected shut down without logout wouldn't lose all the 
        user's progress.
        
    7. Multithreading of slow database operations was facilitated using the thread pool pattern.
        - This was implemented in the class FEAAFacade.