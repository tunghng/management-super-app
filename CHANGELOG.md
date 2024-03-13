# CHANGE LOG

## Update (Aug 07, 2023)
* **Announcement Service v1.1.4 --> v1.1.5**
    * API Count unread
    * Get Announcements:
        * Search relative, eject "" to absolute, use query `isSearchMatchCase`
    * Change to Avatar URL
* **Billing Service v1.1.5 --> v1.1.6**
    * Get Billings:
        * Search relative, eject "" to absolute, use query `isSearchMatchCase`
  * Change to Avatar URL
    * Fix export template
* **Contact Service v1.0.6 --> v1.0.7**
    * Get Contacts:
        * Search relative, eject "" to absolute, use query `isSearchMatchCase`
* **Document Service v1.1.3 --> v1.1.4**
    * Get Documents:
        * Search relative, eject "" to absolute, use query `isSearchMatchCase`
* **Form Service v1.2.7 --> v1.2.8**
    * Get Form Templates:
        * Search relative, eject "" to absolute, use query `isSearchMatchCase`
* **News Service v1.1.5 --> v1.1.6**
    * Get News:
        * Search relative, eject "" to absolute, use query `isSearchMatchCase`
* **SSO Service v2.1.5 --> v2.1.6**
    * Get Users:
        * Search relative, eject "" to absolute, use query `isSearchMatchCase`
* **Support Service v2.1.1 --> v2.1.2**
    * Get Tickets:
        * Search relative, eject "" to absolute, use query `isSearchMatchCase`
    * Fix isRead 
  
## Update (Jul 31, 2023)

* **Announcement Service v1.1.3 --> v1.1.4**
    * Get Announcements:
        * Search Text with relative (default) and match case (with " ")
* **Billing Service v1.1.4 --> v1.1.5**
    * Get Billings:
        * Search Text with relative (default) and match case (with " ")
        * User Avatar with url
* **Contact Service v1.0.5 --> v1.0.6**
    * Get Contacts:
        * Search Text with relative (default) and match case (with " ")
    * Get Contact Avatar By Id:
        * Return png instead of string base 64
* **Document Service v1.1.2 --> v1.1.3**
    * Get Documents:
        * Search Text with relative (default) and match case (with " ")
        * Fix cannot filter with list typeIds and contactIds
    * Save Document:
        * Support `typeId`
        * `type` will not support from Aug 7, 2023
        * Role CUSTOMER no need to send `contactId`
* **Form Service v1.2.6 --> v1.2.7**
    * Get Form Templates:
        * Search Text with relative (default) and match case (with " ")
* **News Service v1.1.4 --> v1.1.5**
    * Get News:
        * Search Text with relative (default) and match case (with " ")
* **SSO Service v2.1.4 --> v2.1.5**
    * Get Users:
        * Search Text with relative (default) and match case (with " ")
        * Change filter `startTs, endTs` --> `createdAtStartTs, createdAtEndTs`
    * Get User Avatar By Id:
        * Return png instead of string base 64
* **Support Service v2.1.0 --> v2.1.1**
    * Get Tickets:
        * Search Text with relative (default) and match case (with " ")
    * Get Ticket By Id:
        * Response `TicketTypeDto` with `id, name, tenantId` instead of type name

## Update (2023.07.28)

* **Support Service v2.0.5 --> v2.1.0**
    * Save Ticket:
        * Support `typeId`
        * `type` will not support from Aug 7, 2023
* **News Service v1.1.3 --> v1.1.4**
    * Get News:
        * Add filter `updatedAtStartTs, updatedAtEndTs`

## Update (2023.07.25)

* **Billing Service v1.1.3 --> v1.1.4**
    * Get Billings:
        * Change filter `startTs, endTs` --> `createdAtStartTs, createdAtEndTs`
        * Add filter `dueDateStartTs, dueDateEndTs`
        * Add filter `updatedAtStartTs, updatedAtEndTs`
* **Contact Service v1.0.4 --> v1.0.5**
    * Get Contacts:
        * Change filter `startTs, endTs` --> `createdAtStartTs, createdAtEndTs`
* **SSO Service v2.1.3 --> v2.1.4**
    * Get Logs:
        * Change filter `startTs, endTs` --> `createdAtStartTs, createdAtEndTs`
* **Support Service v2.0.4 --> v2.0.5**
    * Get Tickets:
        * Change filter `startTs, endTs` --> `createdAtStartTs, createdAtEndTs`
        * Add filter `updatedAtStartTs, updatedAtEndTs`
    * Get Logs:
        * Change filter `startTs, endTs` --> `createdAtStartTs, createdAtEndTs`

## Update (2023.07.21)

* **Contact Service v1.0.3 --> v1.0.4**
    * Export:
        * Match date and time to UI
* **Form Service v1.2.5 --> v1.2.6**
    * Get Form Template Info:
        * Add contactId to response
* **SSO Service v2.1.3 --> v2.1.4**
    * Get Users:
        * Add filter `isEnabled`

## Update & Fix bugs (2023.07.13)

* **Announcement Service v1.1.2 --> v1.1.3**
    * Remove `deleted` and `read` in announcement response
    * Get Announcements:
        * Role Customer: Fix not get filter contact id
        * Search with code
        * Search for role CUSTOMER
    * Fix `isRead` null
    * Update `read` and `unread` flow
* **Billing Service v1.1.2 --> v1.1.3**
    * Remove `deleted` in bill response
* **Document Service v1.1.1 --> v1.1.2**
    * Remove `deleted` in document response
    * Fix: Cannot save exist document
* **Form Service v1.2.4 --> v1.2.5**
    * Remove `deleted` and `read` in form response
* **News Service v1.1.1 --> v1.1.2**
    * Remove `deleted` and `read` in news response
* **SSO Service v2.1.2 --> v2.1.3**
    * Remove `deleted` in response
    * Get Users
        * Upgrade searchText to search full name
    * Add components pair to url (temporary)
        * "Bãi Đậu Xe" : parking-lot --> `api/parking-lot`
        * "Bảo Trì" : maintenance --> `api/maintenance`
        * "Kiểm Soát Ra Vào" : access-control --> `api/access-control`
        * "Đèn Đường" : traffic-light --> `api/traffic-light`
    * Delete unused components
* **Support Service v2.0.3 --> v2.0.4**
    * Remove `deleted` and `read` in ticket response
    * Update CUSTOMER save ticket without contactId

## Update & Fix bugs (2023.06.21)

* **Form Service v1.2.3 --> v1.2.4**
    * Grant Permission for `TENANT`, `MANAGER`, `CUSTOMER`

## Update & Fix bugs (2023.06.16)

* **Announcement Service v1.1.1 --> v1.1.2**
    * Get Announcements:
        * Fix search with symbol `%`
* **Billing Service v1.1.1 --> v1.1.2**
    * Get Bills:
        * Fix search with symbol `%`
* **Contact Service v1.0.2 --> v1.0.3**
    * Get Contacts:
        * Fix search with symbol `%`
* **Document Service v1.1.0 --> v1.1.1**
    * Get Documents:
        * Fix search with symbol `%`
* **File Storage Service v1.1.0 --> v1.1.1**
    * Add API download with no auth
* **Form Service v1.2.2 --> v1.2.3**
    * Get Form Templates:
        * Fix search with symbol `%`
    * Save Form:
        * Response with id instead of message
* **News Service v1.1.0 --> v1.1.1**
    * Get News:
        * Fix search with symbol `%`
* **SSO Service v2.1.1 --> v2.1.2**
    * Get Users:
        * Fix search with symbol `%`
* **Support Service v2.0.2 --> v2.0.3**
    * Get Tickets:
        * Fix search with symbol `%`

## Update & Fix bugs (2023.06.12)

* **Form Service v1.2.1 --> v1.2.2**
    * Get Form Templates:
        * Add `updatedAt`
        * Filter by list of contactId
        * Grant permissions to `TENANT_ADMIN`

## Update & Fix bugs (2023.06.06)

* **Billing Service v1.1.1**
    * Fix null `bill_code`
    * Get Bills:
        * Add filter multi of `state`
* **Form Service v1.2.0 --> v1.2.1**
    * Get Form Templates:
        * Add filter `isDeleted`
* **News Service v1.1.0**
    * Fix null `bill_code`
* **SSO Service v2.1.0 --> v2.1.1**
    * Get Users:
        * Add filter `createdAt` with `startTs, endTs` params
    * Fix save user
* **Support Service v2.0.1 --> v2.0.2**
    * Get Tickets:
        * Add filter multi of `contactId, typeId`
        * Add filter `isDeleted`
    * Export Tickets:
        * Add filter multi of `contactId, typeId`

## Update & Fix bugs (2023.06.02)

* **Announcement Service v1.1.0 --> v1.1.1**
    * Add field `code` in **Announcement Model** type `0000001`
    * Add delete & restore controller
    * Get Announcements:
        * Add filter `isDeleted`
* **Billing Service v1.1.0 --> v1.1.1**
    * Add delete & restore controller
    * Get Billings:
        * Filter by list of `typeId`, list of `contactId` (old is `fromContactId`)
* **Form Service v1.2.0**
    * Nothing changes
* **News Service v1.0.0**
    * Fix `code` in response
    * Hotfix save news
* **SSO Service v2.1.0**
    * Fix save user
* **Support Service v2.0.0 --> v2.0.1**
    * Add delete & restore controller

## Update & Fix bugs (2023.05.29)

* **Chore**
    * Update docker-compose with `iot-service`
* **API Gateway v2.0.0**
    * Update route `iot-service`
* **IoT Service v1.0.0**
    * Release
* **Discovery Server v1.0.0 -> v1.0.1**
    * Update hostname `discovery-server` instead of `hostname`
* **Billing Service v1.0.0 --> v1.1.0**
    * Get Billings
        * Fix filter by contactId
        * Add filter by state, billTypeId, contactId, isDelete
        * Update search text with `code`
    * Add `delete` function
        * Add `isDelete` in response
        * Filter `isDelete`
* **Support Service v2.0.0**
    * Fix run `update_schema.sql`
* **News Service v1.0.0 --> v1.1.0**
    * Add field `code`
    * Get News:
        * Update search text with `code`
* **Document Service v1.0.0 --> v1.1.0**
    * Add field `code`
    * Get Documents:
        * Update search text with `code`
* **SSO Service v2.0.0 --> v2.1.0**
    * Update History Log
    * Fix create tenant without basic plan
    * Fix SYS_ADMIN get user profile
* **Form Service v1.1.0 --> v1.2.0**
    * Get Form Templates:
        * Update search text with `code`
    * Update `code` 6-digit to 7-digit
* **Announcement Service v1.0.0 --> v1.1.0**
    * Add `delete` function
        * Add `isDelete` in response
        * Filter `isDelete`

## IoT Service v1.0.0 (2023.05.26)

* Auto-sync Tenant IPM to cloud with format `<userId>@tma.com.vn`
* Get List Devices
* Get Camera URL of device id

## Discovery Server v1.0.1 (2023.05.29)

* Update hostname `discovery-server` instead of `hostname`

## Update (2023.05.24)

* Document Service
    * Get Documents filter with list typeId, list companyId
* Support Service
    * Personalize `isRead`
    * Get Tickets: remove query param `isRead`
    * Update count tickets controller: query param `isRead`

## API Gateway v2.0.0 (2023.05.24)

* Add filter user plan
* Add iot-service

## SSO Service v2.0.0 (2023.05.24)

* Add user plan: basic, digital, operation
* Detail
    * Get User Profile: update `planName` and `planExpiredIn`
    * Only Sys Admin edit Tenant Subscription Plan
    * Get User Accounts update `planName` and `planExpiredIn` for all roles

## Update (2023.05.23)

* Announcement Service
    * Update Swagger

## Update (2023.05.18)

* Form Service
    * Update 2 no-auth form controller

## Support Service v2.0.0 (2023.05.18)

* Save history log of ticket
* Get logs with filter

## Fix & Update (2023.05.17)

* Form Service:
    * Save Form with attached file
* Support & News & Document & Billing:
    * Validate specific type/category name

## Update (2023.05.16)

* Bill Service:
    * Get Bills: Update filter by Bill Type id

## Update (2023.05.15)

* News Service:
    * Reduce News Page Data fields
    * Get News: Update filter by categoryId
* SSO Service:
    * Update default component when sign up

## Fix & Update (2023.05.11)

* Contact Service
    * Get Contacts: Search Text
* News & Document & Announcement
    * Update Sort Order & Sort Property
* Change: Notification --> Announcement

## Fix (2023.05.10)

* SSO Service:
    * Change Password

## Update (2023.05.09)

* Support Service v1.0.2: Get Tickets query params `type,contactId`
* Form Service v1.1.0:
    * Get Form Templates: Add `countUnread, pathUrl` in response
    * Get Forms: Add `pathUrl, isRead` in response
* File Storage Service v1.1.0:
    * Upload File with no auth (related to Form Service)
* SSO Service v1.1.0:
    * Sign Up function
    * Update Field for App Mobile Information

## Fix Bugs (2023.05.05)

* Form Service: Update String Data in Input Type SELECT, RADIO

## API-Gateway v1.1.0 (2023.05.04)

**Feature**

* Config Form Service and Billing Service

## Billing Service v1.0.0 (2023.05.04)

**Feature**

* Bill:
    * Get: List, Detail
    * Action: Add, Edit, Closed, Paid
* Bill Type:
    * Get: List, Detail
    * Action: Add, Edit
* Payment Method
    * Get: List, Detail
    * Action: Add, Edit
* Supported multi-tenant
* Export Function

## Form Service v1.0.0 (2023.04.28)

**Feature**

- **Form Template**
    - Get: General List, Customer List, Short Info, Detail, Empty Form
    - Action: Save, Public, Remove/Restore
- Get Input Types
- **Form**
    - Get: List By formTemplateId, Filled Form by formId
    - Action: Save, Approve

## Fix Bugs (2023.04.27)

**Support Service v1.0.2**

* Get items with validation

**Contact Service v1.0.2**

* Get items with validation

**File Storage Service v1.0.0**

* Get files upload by users with pagination

## Fix Bugs (2023.04.25)

**Support Service v1.0.2**

* Update APIs read/unread ticket
* Export: Auto-width, fix get items with condition

## Contact Service v1.0.2 (2023.04.24)

**Feature**

* Export with multi-languages: support Vietnamese (vi-VN) and English (en-US)
    * Example: Use request header with "Content-Language: vi-VN"

## Support Service v1.0.2 (2023.04.24)

**Feature**

* Export with multi-languages: support Vietnamese (vi-VN) and English (en-US)
    * Example: Use request header with "Content-Language: vi-VN"
* Update read ticket with `isRead`
* Get Tickets: Update query params `isRead`.

## Fix Bugs (2023.04.24)

**File Storage Service**

* Download file with extension *.doc, *.csv..

## Support Service v1.0.1 (2023.04.19)

**Feature**

* Get Tickets: Update fields `type, startTs, endTs, sortOrder, sortProperty`
    * Dynamic Attributes, Created Time with `startTs` and `endTs`
    * Default
        * Attributes: code, title, type, state, contact.name, closedAt, closedBy, review.title, review.title,
          review.body
        * Created Time: From the beginning
        * Filename: Tickets_yyyy_MM_dd_hh_mm_ss.xlsx

## Form Service v1.0.0 (2023.04.18)

**Feature**

* Form
    * Get Forms with pagination
    * Get Form Detail by id
    * Delete Form by id
    * Save Form
* Form Type
    * Save form type
    * Get Empty Form (to fill value)
    * Delete Form Type
* Customize for each tenant

## Contact Service v1.0.1 (2023.04.18)

**Feature**

* Get Contacts: Update field `startTs, endTs, sortOrder, sortProperty`
* Export Contacts
    * Dynamic Attributes, Created Time with `startTs` and `endTs`
    * Default
        * Attributes: id, name, taxNumber, email, phone, field
        * Created Time: From the beginning
        * Filename: Contacts_yyyy_MM_dd_hh_mm_ss.xlsx

## Support Service v1.0.1 (2023.04.12)

**Feature**

* Update field `code` to each ticket
* Update field `closedAt` when finish the ticket
* Update field `attachedFile` to integrate file into ticket

**Fix bugs**

* Auto execute sql update script `schema_update.sql` on running application

## Update (2023.04.10)

**File Storage Service v1.0.0**

* Update field `extension` in response

**SSO Service v1.0.0**

* Fix bug function `findByEmail`

**Contact Service v1.0.0**

* Remove kafka announcement consumer and controller & service related relationship between contact & announcement

**Announcement Service v1.0.0**

* Update field contact in get Announcements
* Update field `createdAt`,`updatedAt` in get announcement by id
* Integrate `attachedFile` in save, get announcement

## File Storage Service v1.0.0 (2023.04.10)

**Feature**

- Get File Info
- Get File by id
- Get File uploaded by Tenant, User
- Upload File
- Update Info
- Publish File
- Get Multi-Files
- Download File by id

