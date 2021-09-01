import React from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {Link} from 'react-router-dom';

import * as selectors from '../selectors';
import Cities from './Cities';

const FindCitiesResult = () => {

    const cities = useSelector(selectors.getCities);

    if (!cities) {
        return null;
    }

    if (cities.length === 0) {
        return (
            <div>
                <div className="alert alert-info" role="alert">
                    No existen ciudades
                </div>
                <Link to='/business/add-city'>
                    <div className="form-group">
                        <button type="submit" className="btn btn-primary">Añadir</button>
                    </div> 
                </Link>
            </div>
        );
    }

    return (

        <div>
            <Cities cities={cities}/>
            <Link to='/business/add-city'>
                <div className="vertical-center">
                    <button type="submit" className="btn btn-dark">Añadir</button>
                </div> 
            </Link>
        </div>

    );

}

export default FindCitiesResult;
