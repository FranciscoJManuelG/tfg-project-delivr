import {init} from './appFetch';
import * as userService from './userService';
import * as businessService from './businessService';
import * as businessCatalogService from './businessCatalogService';
import * as productCatalogService from './productCatalogService'
import * as productManagementService from './productManagementService'

export {default as NetworkError} from "./NetworkError";

export default {init, userService, businessService, businessCatalogService, 
    productCatalogService, productManagementService};
