import React from 'react';
import {useSelector} from 'react-redux';
import {FormattedMessage} from 'react-intl';
import PropTypes from 'prop-types';


import * as selectors from '../selectors';

const CategorySelectorAddCompany = (selectProps) => {

    const companyCategories = useSelector(selectors.getCompanyCategories);
    
    return (

        <select {...selectProps}>

            {companyCategories && companyCategories.map(companyCategory => 
                <option key={companyCategory.id} value={companyCategory.id}>{companyCategory.name}</option>
            )}

        </select>

    );

}

CategorySelectorAddCompany.propTypes = {
    selectProps: PropTypes.object
};

export default CategorySelectorAddCompany;