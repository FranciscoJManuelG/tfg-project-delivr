import React, {useState} from 'react';
import {useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';
import {useHistory} from 'react-router-dom';

import {Errors} from '../../common';
import * as actions from '../actions';
import CitySelector from '../../business/components/CitySelector';

const AddFavouriteAddress = () => {

    const dispatch = useDispatch();
    const history = useHistory();
    const [street, setStreet] = useState('');
    const [cp, setCp] = useState('');
    const [cityId, setCityId]  = useState(0);
    const [backendErrors, setBackendErrors] = useState(null);
    let form;

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {
            
            dispatch(actions.addFavouriteAddress(street.trim(),
                cp.trim(), toNumber(cityId),
                () => history.push('/users/find-favourite-addresses'),
                errors => setBackendErrors(errors)));

        } else {

            setBackendErrors(null);
            form.classList.add('was-validated');

        }
    }

    const toNumber = value => value.length > 0 ? Number(value) : null;

    return (
        <div className="container">
            <div className="row justify-content-center" >
                <aside className="col-sm-6">
                    <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
                    <div className="card">
                        <article className="card-body">
                	        <h4 className="card-title mb-4 mt-1">Añade una dirección</h4>
                	        <br/>
                            <form ref={node => form = node} className="needs-validation" 
                                noValidate onSubmit={e => handleSubmit(e)}>
                                <div className="form-group">
                                    <label htmlFor="street">Calle: </label>
                                    <input type="text" id="street" className="form-control" 
                                        value={street}
                                        onChange={e => setStreet(e.target.value)}
                                        autoFocus
                                        required
                                        maxLength="60"/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="cp">Código Postal: </label>
                                    <input type="text" id="cp" className="form-control" 
                                        value={cp}
                                        onChange={e => setCp(e.target.value)}
                                        autoFocus
                                        required
                                        maxLength="5"/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="cityId">Ciudad: </label>
                                    <CitySelector id="cityId" className="custom-select my-1 mr-sm-2"
                                    value={cityId} 
                                    onChange={e => setCityId(e.target.value)}/>
                                </div>
                                <br/>
                                <div className="row">
                                    <div className="col">
                                        <div className="form-group">
                                            <button type="submit" className="btn btn-primary btn-block">Añadir</button>
                                        </div> 
                                    </div>
                                </div>                                                              
                            </form>
                        </article>
                    </div>
	            </aside> 
            </div>
        </div> 
    );

}

export default AddFavouriteAddress;