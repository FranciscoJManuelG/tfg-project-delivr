import React from 'react';
import PropTypes from 'prop-types';

import {Link} from 'react-router-dom';


const City = ({city}) => {

    return (
        <tr>                   
            <td>
                {city.name}
            </td>
            <td>
                {city.provinceName}
            </td>
            <td>
                <Link to={`/business/find-city-to-edit/${city.id}`}>
                        <div className="form-group">
                            <button type="submit" className="btn btn-primary btn-sm float-center">
                                <span className="fas fa-edit"></span>
                            </button>
                        </div> 
                </Link>
            </td>
        </tr>
    );

}

City.propTypes = {
    city: PropTypes.object.isRequired,
}

export default City;
