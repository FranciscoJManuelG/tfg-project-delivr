import {config, appFetch} from './appFetch';

export const addCompany = (name, capacity, reserve, homeSale, reservePercentage, companyCategoryId,
    openingTime, closingTime, lunchTime, dinerTime, onSuccess, onErrors) =>
    appFetch(`/business/companies`,
        config('POST', {name, capacity, reserve, homeSale, reservePercentage, companyCategoryId,
            openingTime, closingTime, lunchTime, dinerTime}),
        onSuccess, onErrors);

export const modifyCompany = (company, onSuccess, onErrors) =>
    appFetch(`/business/companies/${company.id}`, config('PUT', company),
        onSuccess, onErrors);

export const blockCompany = (companyId, onSuccess) =>
    appFetch(`/business/companies/${companyId}/block`, 
        config('POST'), onSuccess);

export const unlockCompany = (companyId, onSuccess) =>
    appFetch(`/business/companies/${companyId}/unlock`, 
        config('POST'), onSuccess);

export const deregister = (companyId, onSuccess, onErrors) =>
    appFetch(`/business/companies/${companyId}`, 
        config('DELETE'), onSuccess, onErrors);

export const findCompany = (onSuccess) => 
    appFetch('/business/companies/company', 
        config('GET'), onSuccess);

export const findCompanyById = (companyId, onSuccess) => 
    appFetch(`/business/companies/${companyId}`, 
        config('GET'), onSuccess);
    
export const findAllCompanyCategories = (onSuccess) => 
    appFetch('/business/companies/categories', 
        config('GET'), onSuccess);

export const addCompanyCategory = (name, onSuccess, onErrors) =>
    appFetch(`/business/companyCategories`,
        config('POST', {name}), onSuccess, onErrors);

export const modifyCompanyCategory = (companyCategory, onSuccess, onErrors) =>
    appFetch(`/business/companyCategories/${companyCategory.id}`, config('PUT', companyCategory),
        onSuccess, onErrors);

export const addCompanyAddress = (street, cp, cityId, companyId, onSuccess, onErrors) =>
    appFetch(`/business/companyAddresses`,
        config('POST', {street, cp, cityId, companyId}),
        onSuccess, onErrors);

export const deleteCompanyAddress = (addressId, onSuccess, onErrors) =>
    appFetch(`/business/companyAddresses/${addressId}`,
        config('DELETE'), onSuccess, onErrors);

export const findCompanyAddresses = (companyId, {page}, onSuccess) => 
    appFetch(`/business/companyAddresses?companyId=${companyId}&page=${page}`, 
        config('GET'), onSuccess);

export const findAllCities = (onSuccess) => 
    appFetch('/business/cities', 
        config('GET'), onSuccess);

export const addCity = (name, provinceId, onSuccess, onErrors) =>
    appFetch(`/business/cities`,
        config('POST', {name, provinceId}), onSuccess, onErrors);

export const modifyCity = (city, onSuccess, onErrors) =>
    appFetch(`/business/cities/${city.id}`, config('PUT', city),
        onSuccess, onErrors);

export const findAllProvinces = (onSuccess) => 
    appFetch('/business/provinces', 
        config('GET'), onSuccess);

export const addProvince = (name, onSuccess, onErrors) =>
    appFetch(`/business/provinces`,
        config('POST', {name}), onSuccess, onErrors);

export const modifyProvince = (province, onSuccess, onErrors) =>
    appFetch(`/business/provinces/${province.id}`, config('PUT', province),
        onSuccess, onErrors);

export const findCompanyGoals = (companyId, {page}, onSuccess) => 
    appFetch(`/business/companyGoals?companyId=${companyId}&page=${page}`, 
        config('GET'), onSuccess);

export const addGoal = (companyId, discountType, discountCash, discountPercentage, goalTypeId, goalQuantity, 
    onSuccess, onErrors) =>{
        appFetch(`/business/goals`,
        config('POST', {companyId, discountType, discountCash, discountPercentage, goalTypeId, goalQuantity}),
        onSuccess, onErrors);
    }

export const modifyGoal = (goal, companyId, onSuccess, onErrors) =>
    appFetch(`/business/goals/${goal.id}`, config('PUT', {goal, companyId}),
        onSuccess, onErrors);

export const findGoal = (goalId, companyId, onSuccess) =>
    appFetch(`/business/goals/${goalId}?companyId=${companyId}`, 
        config('GET'), onSuccess);

export const modifyStateGoal = (goalId, companyId, option, onSuccess) =>
    appFetch(`/business/goals/${goalId}/modifyStateGoal`, 
        config('POST', {companyId, option}), onSuccess);

export const findAllGoalTypes = (onSuccess) => 
    appFetch('/business/goals/goalTypes', 
        config('GET'), onSuccess);