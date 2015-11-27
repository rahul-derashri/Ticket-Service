# Ticket-Service
####Simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats####

####Steps to Run Project####
  * Go to the path till "src".
  * Execute command "javac com/usage/BookTicket.java" to compile.
  * Execute command "java com/usage/BookTicket" to run.

####Steps to Test Project####
  * Go to the path till "src"
  * Execute command "javac -cp .;"./../lib/junit-4.12.jar" com/test/TestTicketService.java" to compile.
  * Execute command "java -cp .;"./../lib/junit-4.12.jar";"./../lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore  
    com.test.TestTicketService" to run test cases.

####Assumptions and Comments about Best Seats Algorithm####
  * At most 10 seats can be booked together as usually it is a dropdown on GUI.
  * Seats are assigned sequentially starting with min Venue level and lowest row possible.
  * Seats are considered best if all seats are in one row.
  * If contiguous seats are not available i.e. if the systems not able to find the best seats then it assignes random seats at the 
    lowest level possible as long as the required number of seats are available.
  * As Database is not used for this implementation, SeatsDB is treated as DB.
  * Not used EmailId to check uniqueness as moslty ConfirmationCode and holdSeatId is used to identify the related object details.

####NOTE 
TIMEOUT for Ticket hold expiration is set in the SeatsDB file. While testing you can change it to 20 Seconds to make it run fast  but set it to 60 seconds if you are running it manually to see the proper functioning.
