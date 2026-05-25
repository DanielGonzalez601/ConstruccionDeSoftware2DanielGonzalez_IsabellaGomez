package com.bank.config;

import com.bank.application.port.output.BankAccountRepositoryPort;
import com.bank.application.port.output.UserRepositoryPort;
import com.bank.domain.model.aggregate.BankAccount;
import com.bank.domain.model.entity.User;
import com.bank.domain.model.valueobject.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);
    private final UserRepositoryPort userRepository;
    private final BankAccountRepositoryPort accountRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepositoryPort userRepository, BankAccountRepositoryPort accountRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.existsByIdentificationNumber("DAN001")) { log.info("[SEED] Base de datos ya inicializada."); return; }
        log.info("[SEED] Sembrando datos iniciales...");
        saveUser("Daniel Gonzalez",    "DAN001",  "daniel.gonzalez@bank.com",      "3001234567", LocalDate.of(2000,6,1),   "Calle 10 # 45-20 Medellín",   UserRole.INTERNAL_ANALYST,    "daniel123",     null);
        saveUser("Carlos Restrepo",    "TEL001",  "carlos.restrepo@bank.com",      "3019876543", LocalDate.of(1992,4,18),  "Carrera 5 # 12-30 Bogotá",    UserRole.TELLER,              "teller123",     null);
        saveUser("Isabella Gomez",     "ISA001",  "isabella.gomez@bank.com",       "3024567890", LocalDate.of(2001,9,15),  "Avenida El Poblado # 3-50",   UserRole.COMMERCIAL_EMPLOYEE, "isabella123",   null);
        saveUser("Yorlando Montiel",   "YOR001",  "yorlando.montiel@email.com",    "3032345678", LocalDate.of(1998,3,22),  "Calle 80 # 23-10 Barranquilla",UserRole.CLIENT_INDIVIDUAL,  "yorlando123",   null);
        saveUser("Valentina Torres",   "CLI002",  "valentina.torres@email.com",    "3046789012", LocalDate.of(1995,11,5),  "Carrera 15 # 88-40 Bogotá",   UserRole.CLIENT_INDIVIDUAL,   "vale123",       null);
        saveUser("Empresas Andinas",   "CORP001", "contacto@empresasandinas.com",  "6011234567", LocalDate.of(1980,1,10),  "Calle 100 # 15-60 Piso 8",    UserRole.CLIENT_COMPANY,      "empresa123",    "CORP001");
        saveUser("Alejandro Rios",     "SUP001",  "alejandro.rios@empresas.com",   "3053344556", LocalDate.of(1985,7,25),  "Calle 100 # 15-60 Piso 8",    UserRole.COMPANY_SUPERVISOR,  "supervisor123", "CORP001");
        saveUser("Mariana Castillo",   "EMP001",  "mariana.castillo@empresas.com", "3064455667", LocalDate.of(1999,2,14),  "Calle 100 # 15-60 Piso 8",    UserRole.COMPANY_EMPLOYEE,    "mariana123",    "CORP001");
        saveAccount("ACC0000000001", AccountType.SAVINGS,  "YOR001",  "15000.00",  "USD");
        saveAccount("ACC0000000002", AccountType.CHECKING, "CLI002",  "8500.00",   "USD");
        saveAccount("ACC0000000003", AccountType.BUSINESS, "CORP001", "250000.00", "USD");
        log.info("[SEED] ================================================");
        log.info("[SEED] CREDENCIALES DE PRUEBA (POST /api/auth/login):");
        log.info("[SEED]   Analyst:      ID=DAN001  | Pass=daniel123");
        log.info("[SEED]   Teller:       ID=TEL001  | Pass=teller123");
        log.info("[SEED]   Commercial:   ID=ISA001  | Pass=isabella123");
        log.info("[SEED]   Client 1:     ID=YOR001  | Pass=yorlando123  | Acc=ACC0000000001 $15,000");
        log.info("[SEED]   Client 2:     ID=CLI002  | Pass=vale123      | Acc=ACC0000000002 $8,500");
        log.info("[SEED]   Corp Admin:   ID=CORP001 | Pass=empresa123   | Acc=ACC0000000003 $250,000");
        log.info("[SEED]   Supervisor:   ID=SUP001  | Pass=supervisor123");
        log.info("[SEED]   Co. Employee: ID=EMP001  | Pass=mariana123");
        log.info("[SEED] Swagger UI -> http://localhost:8080/swagger-ui.html");
        log.info("[SEED] H2 Console -> http://localhost:8080/h2-console  (jdbc:h2:mem:bankdb)");
    }

    private void saveUser(String name, String id, String email, String phone, LocalDate birth,
                          String address, UserRole role, String password, String companyId) {
        User user = User.create(name, id, email, phone, birth, address, role);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setCompanyId(companyId);
        userRepository.save(user);
    }

    private void saveAccount(String number, AccountType type, String ownerId, String balance, String currency) {
        BankAccount account = BankAccount.open(number, type, ownerId, currency);
        account.setBalance(new Money(new BigDecimal(balance), currency));
        accountRepository.save(account);
    }
}
