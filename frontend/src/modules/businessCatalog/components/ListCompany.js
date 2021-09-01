import React from 'react';
import PropTypes from 'prop-types';

import * as selectors from '../../business/selectors';

const ListCompany = ({company, cities, onDeleteItem, onBackendErrors}) => {

    const handleDeleteItem = () => {

        onDeleteItem(company.id, 
            () => onBackendErrors(null), 
            backendErrors => onBackendErrors(backendErrors));

    }

    return (
        <tr>                   
            <td>
                {company.name}
            </td>
            <td>
                {company.street}
            </td>
            <td>
                {selectors.getCityName(cities, company.cityId)}
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

ListCompany.propTypes = {
    company: PropTypes.object.isRequired,
    cities: PropTypes.array.isRequired,
    onDeleteItem: PropTypes.func,
    onBackendErrors: PropTypes.func
}

export default ListCompany;
