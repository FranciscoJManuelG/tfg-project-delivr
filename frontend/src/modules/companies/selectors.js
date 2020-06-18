const getModuleState = state => state.companies

export const getCompanyCategories = state =>
    getModuleState(state).companyCategories;

export const getCompanyCategoryName = (companyCategories, id) => {
    
    if (!companyCategories){
        return '';
    }
    
    const companyCategory = companyCategories.find(companyCategory => companyCategory.id === id);
    
    if (!companyCategory){
        return '';
    }

    return companyCategory.name;
}