import React, {useState} from 'react';
import {useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';
import {useHistory} from 'react-router-dom';

import {Errors} from '../../common';
import * as actions from '../actions';
import CompanyCategorySelector from './CompanyCategorySelector';

const AddCompany = () => {

    const dispatch = useDispatch();
    const history = useHistory();
    const [name, setName] = useState('');
    const [capacity, setCapacity] = useState(1);
    const [reserve, setReserve] = useState(true);
    const [homeSale, setHomeSale] = useState(true);
    const [reservePercentage, setReservePercentage] = useState(0);
    const [openingTime, setOpeningTime]  = useState('10:00');
    const [closingTime, setClosingTime]  = useState('23:00');
    const [lunchTime, setLunchTime]  = useState('14:00');
    const [dinerTime, setDinerTime]  = useState('21:00');
    const [companyCategoryId, setCompanyCategoryId]  = useState(0);
    const [backendErrors, setBackendErrors] = useState(null);
    let form;

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {
            
            dispatch(actions.addCompany(name.trim(),
                capacity, reserve, homeSale,
                reservePercentage, toNumber(companyCategoryId),
                openingTime.trim(), closingTime.trim(), 
                lunchTime.trim(), dinerTime.trim(),
                () => history.push('/users/pay-fee'),
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
                	        <h4 className="card-title mb-4 mt-1">Añade tu empresa</h4>
                	        <br/>
                            <form ref={node => form = node} className="needs-validation" 
                                noValidate onSubmit={e => handleSubmit(e)}>
                                <div className="form-group">
                                    <label htmlFor="name">Nombre: </label>
                                    <input type="text" id="name" className="form-control" 
                                        value={name}
                                        onChange={e => setName(e.target.value)}
                                        autoFocus
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="capacity">Capacidad: </label>
                                    <input type="number" id="capacity" min="1" step ="1" className="form-control" 
                                        placeholder="Name"
                                        value={capacity}
                                        onChange={e => setCapacity(e.target.value)}
                                        autoFocus
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div> 
                                <div className="form-group">
                                    <label htmlFor="companyCategoryId">Categoría: </label>
                                    <CompanyCategorySelector id="companyCategoryId" className="custom-select my-1 mr-sm-2"
                                    value={companyCategoryId} 
                                    onChange={e => setCompanyCategoryId(e.target.value)}/>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="reservePercentage">Porcentaje de la señal de reserva: </label>
                                    <input type="number" id="reservePercentage" min="0" step ="1" className="form-control" 
                                        value={reservePercentage}
                                        onChange={e => setReservePercentage(e.target.value)}
                                        autoFocus
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                                <div>
                                    <label htmlFor="reserve">Reserva: </label>
                                    <div className="form-check-inline">
                                        <input type="radio" id="reserve" name="reserve" className="form-check-input" 
                                            value={true}
                                            onChange={e => setReserve(e.target.value)}
                                            autoFocus
                                            checked
                                            required/>
                                        <label className="form-check-label" htmlFor="reserve">Yes</label>
                                    </div>
                                    <div className="form-check-inline">
                                        <input type="radio" id="reserve2" name="reserve" className="form-check-input" 
                                            value={false}
                                            onChange={e => setReserve(e.target.value)}
                                            autoFocus
                                            required/>
                                        <label className="form-check-label" htmlFor="reserve2">No</label>
                                    </div>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="openingTime">Hora de apertura: </label>
                                    <input type="time" id="openingTime" className="form-control" 
                                        value={openingTime}
                                        onChange={e => setOpeningTime(e.target.value)}
                                        autoFocus
                                        autoComplete
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="closingTime">Hora de cierre: </label>
                                    <input type="time" id="closingTime" className="form-control" 
                                        value={closingTime}
                                        onChange={e => setClosingTime(e.target.value)}
                                        autoFocus
                                        autoComplete
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="lunchTime">Hora de las comidas: </label>
                                    <input type="time" id="lunchTime" className="form-control" 
                                        value={lunchTime}
                                        onChange={e => setLunchTime(e.target.value)}
                                        autoFocus
                                        autoComplete
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="dinerTime">Hora de las cenas: </label>
                                    <input type="time" id="dinerTime" className="form-control" 
                                        value={dinerTime}
                                        onChange={e => setDinerTime(e.target.value)}
                                        autoFocus
                                        autoComplete
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                                <div>
                                    <label htmlFor="homeSale">Venta a domicilio: </label>
                                    <div className="form-check-inline">
                                        <input type="radio" id="homeSale" name="homeSale" className="form-check-input" 
                                            value={true}
                                            onChange={e => setHomeSale(e.target.value)}
                                            autoFocus
                                            checked
                                            required/>
                                        <label className="form-check-label" htmlFor="homeSale">Yes</label>
                                    </div>
                                    <div className="form-check-inline">
                                        <input type="radio" id="homeSale2" name="homeSale" className="form-check-input" 
                                            value={false}
                                            onChange={e => setHomeSale(e.target.value)}
                                            autoFocus
                                            required/>
                                        <label className="form-check-label" htmlFor="homeSale2">No</label>
                                    </div>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                                <br/>
                                <div className="row">
                                    <div className="col">
                                        <div className="form-group">
                                            <button type="submit" className="btn btn-primary btn-block">Guardar</button>
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

export default AddCompany;