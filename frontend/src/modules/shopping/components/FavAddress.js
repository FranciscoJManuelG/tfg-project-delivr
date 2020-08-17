import React from 'react';
import PropTypes from 'prop-types';
import {useHistory} from 'react-router-dom';

import * as businessSelectors from '../../business/selectors';

const FavAddress = ({favouriteAddress, cities, companyId, onSelectItem}) => {

    const history = useHistory();

    const handleSelectItem = () => {

        onSelectItem(favouriteAddress.id, 
            () => history.push(`/shopping/purchase-details/${companyId}`));

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
                    <button type="submit" className="btn btn-primary btn-sm"
                        onClick={() => handleSelectItem()}>
                        Utilizar esta dirección
                    </button>
                    &nbsp;
                    &nbsp;
                </span>
            </td>
        </tr>
    );

}

FavAddress.propTypes = {
    favouriteAddress: PropTypes.object.isRequired,
    cities: PropTypes.array.isRequired,
    onSelectItem: PropTypes.func,
}

export default FavAddress;
