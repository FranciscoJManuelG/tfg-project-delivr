import React from 'react';
import PropTypes from 'prop-types';

import * as selectors from '../selectors';

const CompanyAddress = ({companyAddress, cities, onDeleteItem, onBackendErrors}) => {

    const handleDeleteItem = () => {

        onDeleteItem(companyAddress.addressId, 
            () => onBackendErrors(null), 
            backendErrors => onBackendErrors(backendErrors));

    }

    return (
        <tr>                   
            <td>
                {companyAddress.street}
            </td>
            <td>
                {companyAddress.cp}
            </td>
            <td>
                {selectors.getCityName(cities, companyAddress.cityId)}
            </td>
            <td>
                <span>
                    <button type="submit" className="btn btn-danger btn-sm"
                        onClick={() => handleDeleteItem()}>
                        <span className="fas fa-trash-alt"></span>
                    </button>
                    &nbsp;
                    &nbsp;
                </span>
            </td>
        </tr>
    );

}

CompanyAddress.propTypes = {
    companyAddress: PropTypes.array.isRequired,
    cities: PropTypes.array.isRequired,
    onDeleteItem: PropTypes.func,
    onBackendErrors: PropTypes.func
}

export default CompanyAddress;
