import React, {useState} from 'react';
import {useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';
import {useParams, useHistory} from 'react-router-dom';

import {Errors} from '../../common';
import * as actions from '../actions';
import StarRatings from 'react-star-ratings';

const AddEventEvaluation = () => {

    const dispatch = useDispatch();
    const history = useHistory();
    const [points, setPoints] = useState(Number(5));
    const [opinion, setOpinion] = useState('');
    const [backendErrors, setBackendErrors] = useState(null);
    const {id} = useParams();
    let form;
    

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {

            dispatch(actions.addEventEvaluation(
                id,
                points,
                opinion.trim(),
                () => history.push('reservation/evaluation-completed'),
                errors => setBackendErrors(errors)
            ));

        } else {

            setBackendErrors(null);
            form.classList.add('was-validated');

        }
    }

    return (
        <div className="container">
            <div className="row justify-content-center" >
                <aside className="col-sm-6">
                    <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
                    <div className="card">
                        <article className="card-body">
                            <form ref={node => form = node} className="needs-validation" 
                                noValidate onSubmit={e => handleSubmit(e)}>
                                <div className="form-group">
                                    <label htmlFor="points">Valoración: </label>
                                    <StarRatings
                                        rating={points}
                                        starEmptyColor='rgb(232,232,242)'
                                        starRatedColor='rgb(255,195,0)'
                                        starHoverColor='rgb(255,189,51)'
                                        starDimension='15px'
                                        changeRating={(rating) => setPoints(rating)}
                                        name='points'
                                    />
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div> 
                                <div className="form-group">
                                    <label htmlFor="description">Añada una opinión: </label>
                                    <input type="text" id="opinion" className="form-control" 
                                        value={opinion}
                                        onChange={e => setOpinion(e.target.value)}
                                        autoFocus
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div> 
                                <br/>
                                <div className="row">
                                    <div className="col">
                                        <div className="form-group">
                                            <button type="submit" className="btn btn-primary btn-block">Añadir valoración</button>
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

export default AddEventEvaluation;