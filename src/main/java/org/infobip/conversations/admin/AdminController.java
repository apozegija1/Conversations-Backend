package org.infobip.conversations.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/*@RestController
@RequestMapping(value = "/api/admin")
public class AdminController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody Admin admin) {

        if (adminService.getActiveUser(admin.getUsername()) != null)
            return new ResponseEntity<>(new Response(ResultCode.USERNAME_EXIST, USER_NAME_EXIST), HttpStatus.OK);

        admin.setPassword(PasswordUtils.hashPassword(admin.getPassword()));

        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(adminService.save(admin)), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Response> update(@RequestBody Admin admin) {
        Admin dbAdmin = adminService.findById(admin.getId());

        if (dbAdmin != null && !dbAdmin.getId().equals(admin.getId()) && dbAdmin.getStatus() == EntityStatusEnum.ACTIVE.getValue())
            return new ResponseEntity<>(new Response(ResultCode.USERNAME_EXIST, USER_NAME_EXIST), HttpStatus.OK);

        if (admin.getPassword() == null)
            admin.setPassword(dbAdmin.getPassword());
        else
            admin.setPassword(PasswordUtils.hashPassword(admin.getPassword()));

        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(adminService.save(admin)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> read(@PathVariable Long id) {
        logger.info("id : " + id);
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(adminService.findById(id)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Response> readAll(Pageable pageable) {
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(adminService.findAll(pageable)), HttpStatus.OK);
    }

    @GetMapping("/platform_admin")
    public ResponseEntity<Response> readAllPlatFormAdmin(Pageable pageable, @RequestParam(required = false) String searchKey) {
        List<Role> roles = roleService.getByLevelNameLike(SUPER_ADMIN);
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(adminService.findByPage(roles, 0l, 0l, searchKey, "", pageable)), HttpStatus.OK);
//        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(adminService.findAllAdminByLevels(searchKey, roles, pageable)), HttpStatus.OK);
    }

    @GetMapping("/company_admin")
    public ResponseEntity<Response> readAllCompanyAdmin(Pageable pageable,
                                                        @RequestParam(required = false, defaultValue = "0") Long companyId,
                                                        @RequestParam(required =  false, defaultValue = "10") Long status,
                                                        @RequestParam(required = false, defaultValue = "") String searchValue,
                                                        @RequestParam(required = false, defaultValue = "") String createdBy,
                                                        @RequestParam(required = false, defaultValue = "0") Long toDate,
                                                        @RequestParam(required = false, defaultValue = "0") Long fromDate) {
        List<Role> roles = roleService.getByLevelNameLike(COMPANY_ADMIN);
//        CompanyAdminFilters filters = new CompanyAdminFilters(companyId, searchKey, status, createdBy);
//        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(adminService.findAllAdminByFilters(filters, roles, pageable)), HttpStatus.OK);
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(adminService.findCompanyAdminByPage(roles, companyId, status, searchValue, createdBy, pageable,fromDate,toDate)), HttpStatus.OK);
    }

    @GetMapping("/agency_admin")
    public ResponseEntity<Response> readAllAgencyAdmin(Pageable pageable,
                                                       @RequestParam(required = false, defaultValue = "0") Long companyId,
                                                       @RequestParam(required = false, defaultValue = "") String searchKey,
                                                       @RequestParam(required = false, defaultValue = "0") Long status,
                                                       @RequestParam(required = false, defaultValue = "") String createdBy) {
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(adminService.findAgencyAdminByPage(companyId, searchKey,status,createdBy, pageable)), HttpStatus.OK);
    }

    @GetMapping("/getAllAgencySubAgencyAdmin")
    public ResponseEntity<Response> findAllAdminByFilters(Pageable pageable) {
        CompanyAdminFilters filters = new CompanyAdminFilters();
        List<Role> roles = roleService.getByLevelNameLike(AGENCY_ADMIN);
        Page<Admin> admins = adminService.findAllAdminByFilters(filters, roles, pageable);
        ArrayList<Object> res = new ArrayList<>();
        for (Admin admin: admins) {
            Map<String, Object> mapAdmin = new HashMap<>();
            mapAdmin.put("id", admin.getId());
            mapAdmin.put("username", admin.getUsername());
            mapAdmin.put("status", admin.getStatus());
            res.add(mapAdmin);
        }
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(res), HttpStatus.OK);
    }

    @GetMapping("/active_agency_admin")
    public ResponseEntity<Response> readAllAgencyAdminByCompanyId() {
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(adminService.findAllActiveAdminAgency()), HttpStatus.OK);
    }

    @ApiOperation(value = "get all agency Admin by company id", notes = "get all agency Admin by company id")
    @ApiResponses(value = {
            @ApiResponse(code = 401, response = Response.class, message = "INVALID_TOKEN")})
    @GetMapping("/company/agency_admin")
    public ResponseEntity<Response> getAllActiveAgencyAdmin(Pageable pageable, @RequestParam Long companyId, @RequestParam(required = false) String searchKey, @RequestParam(required = false) Long status, @RequestParam(required = false) String createdBy) {
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(adminService.findAgencyAdminByPage(companyId, searchKey,status,createdBy, pageable)), HttpStatus.OK);
    }

    @GetMapping("/company/agency")
    public ResponseEntity<Response> readAllAgencyAdminByCompanyIdAndAgencyId(Pageable pageable, @RequestParam Long companyId, @RequestParam Long agencyId, @RequestParam(required = false) String searchKey, @RequestParam(required = false) Long status, @RequestParam(required = false) String createdBy) {
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(adminService.findAgencyAdminByCompanyIdAndAgency(companyId, agencyId, searchKey,status,createdBy, pageable)), HttpStatus.OK);
    }

    @GetMapping("/drop_down")
    public ResponseEntity<Response> readAllAgencyAdminForDropDown(HttpServletRequest req, @RequestParam(required = false) Integer type, @RequestParam(required = false) Boolean addEventDropDown,
                                                                  @RequestParam(required = false) Long companyId, @RequestParam(required = false) Long agencyId, @RequestParam(required = false) Boolean includeAll) {
        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
        if (admin == null) {
            return ResponseEntity.ok(new Response(ResultCode.INVALID_ACESS, FAIL));
        }
        if (addEventDropDown == null) {
            addEventDropDown = false;
        }
        if (includeAll == null) {
            includeAll = false;
        }
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(adminService.findAllAdminForDropDown(admin, type, addEventDropDown, companyId, agencyId, includeAll)), HttpStatus.OK);
    }

    @GetMapping(value = "/isAccountNameAvail/{accountName}/{accountId}")
    public ResponseEntity<Boolean> isAccountNameAvail(@PathVariable String accountName, @PathVariable() Long accountId) {
        return ResponseEntity.ok(adminService.isAccountNameAvail(accountName, accountId));
    }

    @GetMapping(value = "/isAccountNameAvail/{accountName}")
    public ResponseEntity<Boolean> isAccountNameAvail(@PathVariable String accountName) {
        return ResponseEntity.ok(adminService.isAccountNameAvail(accountName));
    }

    @PutMapping("/change_password")
    public ResponseEntity<Response> changePassword(@RequestBody Admin admin) {
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESSFULLY_CHANGED_PASSWORD).setResult(adminService.changePassword(admin.getId(), admin.getPassword())), HttpStatus.OK);
    }

    @PutMapping("/access_control_setting")
    public ResponseEntity<Response> updateAccessControlSetting(@RequestBody Admin admin) {
        return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESSFULLY_AGENCY_ADMIN_SETTING).setResult(adminService.updateAccessControlSetting(admin)), HttpStatus.OK);
    }

    @GetMapping("/admin_drop_down")
    public ResponseEntity<Response> findAllForDropDownList(@RequestParam(required = false) Boolean isFromMobile) {
        if (isFromMobile == null || !isFromMobile) {
            return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(adminService.findAllForDropDownList()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Response(ResultCode.SUCCESS, SUCCESS).setResult(adminService.findAllForDropDownListForMobile()), HttpStatus.OK);
        }
    }

    @GetMapping("/stage_state")
    public ResponseEntity<Response> updateStageState(@RequestParam Long adminId, @RequestParam String newState) {
        Response response = new Response(ResultCode.SUCCESS, SUCCESS).setResult(adminService.updateStageState(adminId, newState));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Update stage state", notes = "Update stage state")
    @ApiResponses(value = {
            @ApiResponse(code = 401, response = Response.class, message = "INVALID_TOKEN")})
    @GetMapping("/adminbyid")
    public ResponseEntity<Response> updateStageState(@RequestParam Long adminId) {
        Response response = new Response(ResultCode.SUCCESS, SUCCESS).setResult(adminService.getAdminById(adminId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}*/

