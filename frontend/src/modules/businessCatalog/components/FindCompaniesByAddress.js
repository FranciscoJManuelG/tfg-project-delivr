import React, {useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {useHistory} from 'react-router-dom';
import {FormattedMessage} from 'react-intl';

import CitySelector from '../../business/components/CitySelector';
import * as actions from '../actions';
import users from '../../users';


const FindCompaniesByAddress = () => {

    const role = useSelector(users.selectors.getRole);
    const dispatch = useDispatch();
    const history = useHistory();
    const [cityId, setCityId] = useState('');
    const [street, setStreet] = useState('');

    const handleSubmit = event => {
        event.preventDefault();
        dispatch(actions.findCompanies(
            {companyCategoryId: null,
            cityId: toNumber(cityId),
            street: street.trim(), 
            keywords: "", 
            page: 0}));
        history.push('/businessCatalog/find-companies-result');
    }

    const toNumber = value => value.length > 0 ? Number(value) : null;

    return (

       
        <form className="form-inline mt-2 mt-md-0 justify-content-center" onSubmit={e => handleSubmit(e)}>
            {role !== "ADMIN" &&
                <div className="card">
                    <article className="card-body">
                        <div>
                            <label>Introduzca el nombre de la calle o seleccione una ciudad para buscar restaurantes </label>
                        </div>
                        <br/>
                        <CitySelector id="cityId" className="custom-select my-1 mr-sm-2"
                            value={cityId} onChange={e => setCityId(e.target.value)}/>
                        <input id="street" placeholder="Nombre de la calle" type="text" className="form-control mr-sm-2"
                            value={street} onChange={e => setStreet(e.target.value)}/>
                        
                        <button type="submit" className="btn btn-primary my-2 my-sm-0">
                            <FormattedMessage id='project.global.buttons.search'/>
                        </button>
                    </article>
                </div>
            }
        </form>

    );

}

export default FindCompaniesByAddress;
