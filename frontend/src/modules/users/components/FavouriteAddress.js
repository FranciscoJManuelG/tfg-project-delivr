import React from 'react';
import PropTypes from 'prop-types';

import * as businessSelectors from '../../business/selectors';

const FavouriteAddress = ({favouriteAddress, cities, onDeleteItem, onBackendErrors}) => {

    const handleDeleteItem = () => {

        onDeleteItem(favouriteAddress.id, 
            () => onBackendErrors(null), 
            backendErrors => onBackendErrors(backendErrors));

    }

    return (
        <tr>                   
            <td>
                {favouriteAddress.street}
            </td>
            <td>
                {favouriteAddress.cp}
            </td>
            <td>
                {businessSelectors.getCityName(cities, favouriteAddress.cityId)}
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

FavouriteAddress.propTypes = {
    favouriteAddress: PropTypes.object.isRequired,
    cities: PropTypes.array.isRequired,
    onDeleteItem: PropTypes.func,
    onBackendErrors: PropTypes.func
}

export default FavouriteAddress;
