package app;

import model.Admin;
import model.Passenger;
import model.Route;
import model.Station;
import model.Train;
import model.Ticket;

import enums.TicketType;
import enums.TicketStatus;
import exception.FileProcessingException;
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
        trainService = new TrainService();
        userService = new UserService();

        ticketService = new TicketService(fareCalculator, userService);
        userModuleTest = new UserModuleTest(this, userService);
    }
	
	public static void main(String[] args) {
		SmartMetroTicketingSystem system = new SmartMetroTicketingSystem();
	    system.mainMenu();
	}
	
	//***************************Main Menu***************************
	public void mainMenu() {
		// Load Data
		userService.loadUsers();
		stationService.loadStations();
		trainService.loadTrains();
		// Load Data - routes
		try
		{
		    routeService.loadRoutes(stationService.getStations());
		}
		catch (FileProcessingException e)
		{
		    System.out.println("Could not load routes: " + e.getMessage());
		}

		// Load Data - tickets AFTER users and stations
		ticketService.loadTickets(
		        userService.getPassengers(),
		        stationService.getStations()
		);
		
		System.out.println();
		
        
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
            	  	if (!routeService.viewRoutes()) {
            	        break;
            	    }
            	    String routeId = userModuleTest.readString("Enter Route ID: ");
            	    Route route = routeService.findRouteById(routeId);
            	    if (route == null) {
            	        System.out.println("Route not found.");
            	        break;
            	    }
            	    TicketType type = selectTicketType(route);
            	    if (type != null) {
            	        String confirm = userModuleTest.readString("Confirm purchase? (Y/N): ");
            	        if (confirm.equalsIgnoreCase("Y") || confirm.equalsIgnoreCase("y")) {
            	            ticketService.buyTicket(passenger, route, type);
            	        } 
            	        else {
            	            System.out.println("Ticket purchase cancelled.");
            	        }
            	    }
            	}
              case 4 -> {
    				boolean hasActiveTickets = ticketService.viewActiveTickets(passenger);

   			 		if (!hasActiveTickets) {
        				break;
    				}

    				String ticketId = userModuleTest.readString("Enter ticket ID: ");

    				try {
        				Ticket ticket = ticketService.findTicketById(ticketId);

        				// Make sure the ticket belongs to this passenger
        				if (!ticket.getPassenger().getUserId().equals(passenger.getUserId())) {
            				System.out.println("Ticket not found.");
            				break;
        				}

        				// Make sure the ticket is active
        				if (ticket.getStatus() != TicketStatus.ACTIVE) {
            				System.out.println("Only active tickets can be cancelled.");
            				break;
        				}

        				String confirmation = userModuleTest.readString(
                				"Are you sure you want to cancel ticket " + ticketId + "? (Y/N): ");

        				if (confirmation.equalsIgnoreCase("Y")) {
            				ticketService.cancelTicket(ticketId);
        				} else {
            				System.out.println("Ticket cancellation cancelled.");
        				}
    				}
    				catch (TicketNotFoundException e) {
        				System.out.println(e.getMessage());
    				}
				}
              case 5 -> ticketService.viewTickets(passenger);
              case 0 -> {
            	  System.out.println();
                  System.out.println("Passenger logged out.");
                  loggedIn = false;
              }
              default -> System.out.println("Invalid choice. Please enter number 0 - 5.");
          }
          System.out.println();
      }
  }
        
    private void printPassengerMenu() {
        System.out.println("========== PASSENGER DASHBOARD ==========");
        System.out.println("1. View Profile");
        System.out.println("2. Top Up Balance");
        System.out.println("3. Buy Ticket");
        System.out.println("4. Cancel Ticket");
        System.out.println("5. View Ticket");
        System.out.println("0. Logout");
        System.out.println("=========================================");
    }
    
    public TicketType selectTicketType(Route route) {
    System.out.println("\nSelect Ticket Type:");

    double singleFare = fareCalculator.calculateFare(route, TicketType.SINGLE);
    double dailyFare = fareCalculator.calculateFare(route, TicketType.DAILY);
    double monthlyFare = fareCalculator.calculateFare(route, TicketType.MONTHLY);

    System.out.printf("1. Single       - RM %.2f%n", singleFare);
    System.out.printf("2. Daily Pass   - RM %.2f%n", dailyFare);
    System.out.printf("3. Monthly Pass - RM %.2f%n", monthlyFare);

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
	        System.out.println();
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
        System.out.println("4. View All Users");
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
		        }
	            case 2 -> stationService.viewStations();
	            //case 3 -> stationService.searchStation(name);
	            case 3 -> {
	                String name = userModuleTest.readString("Enter station name: ");
	                System.out.println();
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
    	//System.out.println();
        System.out.println("=========== STATION MANAGEMENT ==========");
        System.out.println("1. Add Station");
        System.out.println("2. View Stations");
        System.out.println("3. Search Station");
        System.out.println("0. Return");
        System.out.println("=========================================");
    }
    
    
	//Train Management
	private void trainManagement() {
	    boolean running = true;
	    while (running) {
	        printTrainMenu();
	        int choice = userModuleTest.readInt("Enter choice: ");
	        switch (choice) {
		        case 1 -> {
	                String trainId = userModuleTest.readString("Enter train ID: ");
	                String trainName = userModuleTest.readString("Enter train name: ");
	                int capacity = userModuleTest.readInt("Enter train capacity: ");
	
	                Train train = new Train(trainId, trainName, capacity);
	                trainService.addTrain(train);
	            }

	            case 2 -> trainService.viewTrains();
	            case 0 -> running = false;
	            default -> System.out.println("Invalid choice. Please enter number 0 - 2.");
	        }
	        System.out.println();
	    }
	}
	
    private void printTrainMenu() {
    	//System.out.println();
        System.out.println("============ TRAIN MANAGEMENT ===========");
        System.out.println("1. Add Train");
        System.out.println("2. View Trains");
        System.out.println("0. Return");
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
	            	stationService.viewStations();

	                String routeId = userModuleTest.readString("Enter Route ID: ");

	                String sourceId = userModuleTest.readString("Enter Source Station ID: ");
	                Station source = stationService.findStationById(sourceId);
	                if (source == null) {
	                    System.out.println("Source station not found.");
	                    break;
	                }

	                String destinationId = userModuleTest.readString("Enter Destination Station ID: ");
	                Station destination = stationService.findStationById(destinationId);
	                if (destination == null) {
	                    System.out.println("Destination station not found.");
	                    break;
	                }

	                double distance = userModuleTest.readDouble("Enter Distance (km): ");

	                Route route = new Route(routeId, source, destination, distance);
	                routeService.addRoute(route);
	            }	            
	            case 2 -> routeService.viewRoutes();
	            case 0 -> running = false;
	            default -> System.out.println("Invalid choice. Please enter number 0 - 2.");
	        }
	        System.out.println();
	    }
	}
	
    private void printRouteMenu() {
    	//System.out.println();
        System.out.println("============ ROUTE MANAGEMENT ===========");
        System.out.println("1. Create Route");
        System.out.println("2. View Routes");
        System.out.println("0. Return");
        System.out.println("=========================================");
    }
    
}
