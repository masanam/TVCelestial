package id.co.kynga.app.general.controller;


import id.co.kynga.app.model.MeModel;

public abstract class MeCallback {
	public abstract void onSuccess(final MeModel me_model);

	public abstract void onFailure();
}