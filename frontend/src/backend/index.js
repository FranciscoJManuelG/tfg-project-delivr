import {init} from './appFetch';
import * as userService from './userService';
import * as businessService from './businessService';

export {default as NetworkError} from "./NetworkError";

export default {init, userService, businessService};
