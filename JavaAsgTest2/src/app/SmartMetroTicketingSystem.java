package app;

//import java.util.InputMismatchException;
//import java.util.Scanner;

//import enums.TicketType;
//import exception.TicketNotFoundException;
import model.Admin;
import model.Passenger;
import model.Route;
import model.Station;
import model.Train;

//import app.UserModuleTest;
import enums.TicketType;
//import enums.UserRole;
import exception.TicketNotFoundException;
import fare.FareCalculator;
import fare.StandardFareCalculator;
import service.ReportService;
import service.RouteService;
import service.StationService;
import service.TicketService;
import service.TrainService;
import service.UserService;

public class SmartMetroTicketingSystem {
	
	/*//Clear previous output each time when enter the next menu.
	public static void clearScreen() {
	    for (int i = 0; i < 30; i++) {
	        System.out.println();
	    }
	}*/
	
	//private static final Admin admin = new Admin();
	//private static final Passenger passenger = new Passenger();
	
//	private static final FareCalculator fareCalculator = new StandardFareCalculator();
//	private static final UserService userService = new UserService();
//	private static final UserModuleTest userModuleTest = new UserModuleTest(userService);
//	private static final ReportService reportService = new ReportService();
//	private static final RouteService routeService = new RouteService();
//	private static final StationService stationService = new StationService();
//	private static final TicketService ticketService = new TicketService(fareCalculator);
//	private static final TrainService trainService = new TrainService();

	private final FareCalculator fareCalculator;
    private final UserModuleTest userModuleTest;
    private final ReportService reportService;
    private final RouteService routeService;
    private final StationService stationService;
    private final TicketService ticketService;
    private final TrainService trainService;
    private final UserService userService;

    public SmartMetroTicketingSystem() {

        fareCalculator = new StandardFareCalculator();
        reportService = new ReportService();
        routeService = new RouteService();
        stationService = new StationService();
        ticketService = new TicketService(fareCalculator);
        trainService = new TrainService();
        userService = new UserService();

        userModuleTest = new UserModuleTest(this, userService);
    }
	
	public static void main(String[] args) {
		SmartMetroTicketingSystem system = new SmartMetroTicketingSystem();
	    system.mainMenu();
	}
	
	//***************************M Menu***************************
	public void mainMenu() {
		// Load any previously saved users at startup.
        userService.loadUsers();
        
    	// Seed a default admin the first time the program runs.
        if (userService.findByEmail("admin@metro.com") == null) {
            Admin admin = new Admin(userService.generateUserId(),
                    "System Admin", "admin@metro.com", "admin123");
            userService.register(admin);
        }
		
		boolean running = true;
		while (running) {
        	printMainMenu();
            int mainChoice = userModuleTest.readInt("Enter choice: ");
            switch (mainChoice) {
                case 1 -> userModuleTest.registerPassenger();
                case 2 -> userModuleTest.login();
                case 0 -> {
                    userService.saveUsers(); // save before exit
                    System.out.println("Thank you for your visiting. See You!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please enter number 0 - 2.");
            }
            System.out.println();
        }
	}
	
	private void printMainMenu() {
        System.out.println("====================================");
        System.out.println("    SMART METRO TICKETING SYSTEM    ");
        System.out.println("====================================");
        System.out.println("1. Registration");
        System.out.println("2. Login");
        System.out.println("0. Exit");
        System.out.println("====================================");
    }


	
	//***************************Passenger Menu***************************
    public void passengerMenu(Passenger passenger) {
      boolean loggedIn = true;
      passenger.showDashboard();
      
      while (loggedIn) {
          printPassengerMenu();
          int psgChoice = userModuleTest.readInt("Enter choice: ");
          switch (psgChoice) {
              case 1 -> passenger.viewProfile();
              case 2 -> userModuleTest.topUp(passenger);
              case 3 -> {
            	    routeService.viewRoutes();

            	    String routeId = userModuleTest.readString("Enter Route ID: ");
            	    Route route = routeService.findRouteById(routeId);
            	    if (route == null) {
            	        System.out.println("Route not found.");
            	        break;
            	    }
            	    TicketType type = selectTicketType();
            	    if (type != null) {
            	        ticketService.buyTicket(passenger, route, type);
            	    }
            	}
              case 4 -> {
                  String ticketId = userModuleTest.readString("Enter ticket ID to cancel: ");
                  try {
                      ticketService.cancelTicket(ticketId);
                  } catch (TicketNotFoundException e) {
                      System.out.println(e.getMessage());
                  }
              }
              case 5 -> ticketService.viewTickets(passenger);
              case 0 -> {
                  System.out.println("Passenger logged out.");
                  loggedIn = false;
              }
              default -> System.out.println("Invalid choice. Please enter number 0 - 5.");
          }
          System.out.println();
      }
  }
        
    private void printPassengerMenu() {
    	System.out.println();
        System.out.println("========== PASSENGER DASHBOARD ==========");
        System.out.println("1. View Profile");
        System.out.println("2. Top Up Balance");
        System.out.println("3. Buy Ticket");
        System.out.println("4. Cancel Ticket");
        System.out.println("5. View Ticket");
        System.out.println("0. Logout");
        System.out.println("=========================================");
    }
    
    public TicketType selectTicketType() {
    System.out.println("\nSelect Ticket Type:");
    System.out.println("1. Single");
    System.out.println("2. Daily Pass");
    System.out.println("3. Monthly Pass");

    int choice = userModuleTest.readInt("Enter choice: ");

	    switch (choice) {
	        case 1:
	            return TicketType.SINGLE;
	        case 2:
	            return TicketType.DAILY;
	        case 3:
	            return TicketType.MONTHLY;
	        default:
	            System.out.println("Invalid ticket type.");
	            return null;
	    }
    }
    
    
    
    //***************************Admin Menu***************************   
	public void adminMenu(Admin admin) {
	    boolean loggedIn = true;
	    admin.showDashboard();
	    
	    while (loggedIn) {
	        printAdminMenu();
	        int admChoice = userModuleTest.readInt("Enter choice: ");
	        switch (admChoice) {
	            case 1 -> stationManagement();
	            case 2 -> trainManagement();
	            case 3 -> routeManagement();
	            case 4 -> userService.viewAllUsers();
	            case 5 -> reportService.generateTicketReport(ticketService.getTickets());
	            case 0 -> {
	                System.out.println("Admin logged out.");
	                loggedIn = false;
	            }
	            default -> System.out.println("Invalid choice. Please enter number 0 - 5.");
	        }
	        System.out.println();
	    }
	}

    private void printAdminMenu() {
        System.out.println("============ ADMIN DASHBOARD ============");
        System.out.println("1. Station Management");
        System.out.println("2. Train Management");
        System.out.println("3. Route Management");
        System.out.println("4. Users Management");
        System.out.println("5. Generate Report");
        System.out.println("0. Logout");
        System.out.println("=========================================");
    }
    
    
    //Station Management
	private void stationManagement() {
	    boolean running = true;
	    while (running) {
	        printStationMenu();
	        int choice = userModuleTest.readInt("Enter choice: ");
	        switch (choice) {
	            //case 1 -> stationService.addStation(null);
		        case 1 -> {
		            String id = userModuleTest.readString("Enter station ID: ");
		            String name = userModuleTest.readString("Enter station name: ");
		            String location = userModuleTest.readString("Enter location: ");
	
		            Station station = new Station(id, name, location);
		            stationService.addStation(station);
	
		            System.out.println("Station added successfully.");
		        }
	            case 2 -> stationService.viewStations();
	            //case 3 -> stationService.searchStation(name);
	            case 3 -> {
	                String name = userModuleTest.readString("Enter station name: ");
	                Station station = stationService.searchStation(name);

	                if (station != null) {
	                    station.displayInfo();
	                } 
	                else {
	                    System.out.println("Station not found.");
	                }
	            }
	            case 0 -> running = false;
	            default -> System.out.println("Invalid choice. Please enter number 0 - 3.");
	        }
	        System.out.println();
	    }
	}
	
    private void printStationMenu() {
    	System.out.println();
        System.out.println("=========== STATION MANAGEMENT ==========");
        System.out.println("1. Add Station");
        System.out.println("2. View Stations");
        System.out.println("3. Search Station");
        System.out.println("0. Back");
        System.out.println("=========================================");
    }
    
    
	//Train Management
	private void trainManagement() {
	    boolean running = true;
	    while (running) {
	        printTrainMenu();
	        int choice = userModuleTest.readInt("Enter choice: ");
	        switch (choice) {
	            //case 1 -> trainService.addTrain(null);
		        case 1 -> {
	                String trainId = userModuleTest.readString("Enter train ID: ");
	                String trainName = userModuleTest.readString("Enter train name: ");
	                int capacity = userModuleTest.readInt("Enter train capacity: ");
	
	                Train train = new Train(trainId, trainName, capacity);
	                if (trainService.addTrain(train))
	                {
	                    System.out.println("Train added successfully.");
	                }
	                else
	                {
	                    System.out.println("Train ID already exists.");
	                }
	            }

	            case 2 -> trainService.viewTrains();
	            case 0 -> running = false;
	            default -> System.out.println("Invalid choice. Please enter number 0 - 2.");
	        }
	        System.out.println();
	    }
	}
	
    private void printTrainMenu() {
    	System.out.println();
        System.out.println("============ TRAIN MANAGEMENT ===========");
        System.out.println("1. Add Train");
        System.out.println("2. View Trains");
        System.out.println("0. Back");
        System.out.println("=========================================");
    }
    
    
	//Route Management
	private void routeManagement() {
	    boolean running = true;
	    while (running) {
	        printRouteMenu();
	        int choice = userModuleTest.readInt("Enter choice: ");
	        switch (choice) {
	            case 1 -> {
	                String routeId = userModuleTest.readString("Enter Route ID: ");
	                String sourceId = userModuleTest.readString("Enter Source Station ID: ");
	                String destinationId = userModuleTest.readString("Enter Destination Station ID: ");
	                double distance = userModuleTest.readDouble("Enter Distance (km): ");

	                Station source = stationService.findStationById(sourceId);
	                Station destination = stationService.findStationById(destinationId);

	                if (source == null) {
	                    System.out.println("Source station not found.");
	                    break;
	                }

	                if (destination == null) {
	                    System.out.println("Destination station not found.");
	                    break;
	                }

	                Route route = new Route(
	                    routeId,
	                    source,
	                    destination,
	                    distance
	                );

	                routeService.addRoute(route);
	            }	            
	            case 2 -> routeService.viewRoutes();
	            case 3 -> {
	                String sourceId = userModuleTest.readString("Enter Source Station ID: ");
	                String destinationId = userModuleTest.readString("Enter Destination Station ID: ");

	                Station source = stationService.findStationById(sourceId);
	                Station destination = stationService.findStationById(destinationId);

	                if (source == null || destination == null) {
	                    System.out.println("Station not found.");
	                    break;
	                }

	                Route route = routeService.findRoute(source, destination);

	                if (route == null) {
	                    System.out.println("Route not found.");
	                } else {
	                    System.out.println("Route found:");
	                    route.displayRoute();
	                }
	            }
	            case 0 -> running = false;
	            default -> System.out.println("Invalid choice. Please enter number 0 - 3.");
	        }
	        System.out.println();
	    }
	}
	
    private void printRouteMenu() {
    	System.out.println();
        System.out.println("============ ROUTE MANAGEMENT ===========");
        System.out.println("1. Add Route");
        System.out.println("2. View Routes");
        System.out.println("3. Search Route");
        System.out.println("0. Back");
        System.out.println("=========================================");
    }
    
}