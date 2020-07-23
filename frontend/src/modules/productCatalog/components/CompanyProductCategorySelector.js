import React from 'react';
import {useSelector} from 'react-redux';
import PropTypes from 'prop-types';


import * as selectors from '../selectors';

const CompanyProductCategorySelector = (selectProps) => {

    const productCategories = useSelector(selectors.getProductCategories);
    
    return (

        <select {...selectProps}>

            {productCategories && productCategories.map(productCategory => 
                <option key={productCategory.id} value={productCategory.id}>{productCategory.name}</option>
            )}

        </select>

    );

}

CompanyProductCategorySelector.propTypes = {
    selectProps: PropTypes.object
};

export default CompanyProductCategorySelector;
