
FILENAME_REGEX = ///
	topology[.]snapshot[.]
	(\d{4})-(\d{2})-(\d{2}) # yyyy-MM-dd
	_
	(\d{2})[.](\d{2})[.](\d{2}) # HH.mm.ss
	[.](\w+) # extension
///

CURRENT_REGEX = ///
	topology[.]snapshot[.]current[.](\w+)
///

parseSnapshot = (filename) ->
	groups = filename.match(FILENAME_REGEX)	
	if groups?
		snapshot =
			id: filename
			date: "#{groups[2]}/#{groups[3]}/#{groups[1]} #{groups[4]}:#{groups[5]}:#{groups[6]}"
			format: groups[7]
	else
		groups = filename.match(CURRENT_REGEX)
		snapshot = 
			id: filename
			date: "current"
			format: groups[1]
	snapshot

window.SnapshotListCtrl = ($scope, $http, $route, $location, $dialog) ->
	
	$scope.topics = $http.get("/service/snapshot/files")
		.success (data) ->
			snapshots = []
			for datum in data
				snapshots.push parseSnapshot(datum)
			$scope.snapshots = snapshots
	
	$scope.clone = (snapshotId) ->
		$location.path "/snapshots/#{snapshotId}/clone"
	
	$scope.openExportDialog = ->
		d = $dialog.dialog(exportModalOpts)
		d.open().then (shouldExport) ->
			if shouldExport
				$http.post("/service/snapshot/export").success -> $route.reload()
	
	$scope.openMergeDialog = (snapshot) ->
		d = $dialog.dialog(mergeModalOpts)
		d.open().then (shouldMerge) ->
			if shouldMerge
				url = "/service/snapshot/merge-current"
				opts = getConfirmationOpts("Current snapshot was merged successfully!")
				if snapshot?
					url = "/service/snapshot/merge/#{snapshot}"
					opts = getConfirmationOpts("Snapshot '#{snapshot}' was merged successfully!")
				$http.post(url).success ->
					$dialog.dialog(opts).open()
				
	
	$scope.openImportDialog = (snapshot) ->
		d = $dialog.dialog(importModalOpts)
		d.open().then (shouldImport) ->
			if shouldImport
				url = "/service/snapshot/import-current"
				opts = getConfirmationOpts("Current snapshot was imported successfully!")
				if snapshot?
					url = "/service/snapshot/import/#{snapshot}"
					opts = getConfirmationOpts("Snapshot '#{snapshot}' was imported successfully!")
				$http.post(url).success ->
					$dialog.dialog(opts).open()
						
	

window.SnapshotViewCtrl = ($scope, $http, $location, $routeParams, $dialog) ->
	
	$scope.snapshotId = $routeParams.snapshotId
	
	$scope.snapshot = parseSnapshot $routeParams.snapshotId
	
	$scope.content = $http.get("/service/snapshot/file/#{$routeParams.snapshotId}")
		.success (data) ->
			$scope.content = data
	
	$scope.clone = ->
		$location.path "/snapshots/#{$routeParams.snapshotId}/clone"
	
	$scope.openMergeDialog = ->
		d = $dialog.dialog(mergeModalOpts)
		d.open().then (shouldMerge) ->
			if shouldMerge
				opts = getConfirmationOpts("Snapshot '#{$scope.snapshotId}' was merged successfully!")
				$http.post("/service/snapshot/merge/#{$scope.snapshotId}").success ->
					$dialog.dialog(opts).open()
	
	$scope.openImportDialog = ->
		d = $dialog.dialog(importModalOpts)
		d.open().then (shouldImport) ->
			if shouldImport
				opts = getConfirmationOpts("Snapshot '#{$scope.snapshotId}' was imported successfully!")
				$http.post("/service/snapshot/import/#{$scope.snapshotId}").success ->
					$dialog.dialog(opts).open()
			
			
window.SnapshotCloneCtrl = ($scope, $http, $location, $routeParams, $dialog) ->

	$scope.snapshotId = $routeParams.snapshotId
	
	$scope.snapshot = parseSnapshot $routeParams.snapshotId
	
	$scope.content = $http.get("/service/snapshot/file/#{$routeParams.snapshotId}")
		.success (data) ->
			$scope.content = data
			
	$scope.openMergeDialog = ->
		d = $dialog.dialog(mergeModalOpts)
		d.open().then (shouldMerge) ->
			if shouldMerge
				opts = getConfirmationOpts("Snapshot was merged successfully!")
				postOpts = 
					headers: 
						"Content-Type": "text/plain"
				$http.post("/service/snapshot/merge", $scope.content, postOpts).success ->
					$dialog.dialog(opts).open()

	$scope.openImportDialog = ->
		d = $dialog.dialog(importModalOpts)
		d.open().then (shouldImport) ->
			if shouldImport
				opts = getConfirmationOpts("Snapshot was imported successfully!")
				postOpts = 
					headers: 
						"Content-Type": "text/plain"
				$http.post("/service/snapshot/import", $scope.content, postOpts).success ->
					$dialog.dialog(opts).open()

exportModalOpts = 
	backdrop: true
	keyboard: true
	backdropClick: true
	backdropFade: true
	dialogFade: true
	templateUrl: 'partials/modal-snapshot-export.html',
	controller: 'SnapshotDialogController'

mergeModalOpts = 
	backdrop: true
	keyboard: true
	backdropClick: true
	backdropFade: true
	dialogFade: true
	templateUrl: 'partials/modal-snapshot-merge.html',
	controller: 'SnapshotDialogController'
			
importModalOpts = 
	backdrop: true
	keyboard: true
	backdropClick: true
	backdropFade: true
	dialogFade: true
	templateUrl: 'partials/modal-snapshot-import.html',
	controller: 'SnapshotDialogController'

getConfirmationOpts = (message) ->
	confirmationModalOpts = 
		backdrop: true
		keyboard: true
		backdropClick: true
		backdropFade: true
		dialogFade: true
		resolve: { message: -> angular.copy(message) }
		templateUrl: 'partials/modal-snapshot-success.html',
		controller: 'ConfirmationDialogController'
	confirmationModalOpts
	

window.SnapshotDialogController = ($scope, $http, dialog) ->
	$scope.doit = -> dialog.close(true)
	$scope.cancel = -> dialog.close(false)
	
window.ConfirmationDialogController = ($scope, $http, dialog, message) ->
	$scope.ok = -> dialog.close()
	$scope.message = message
	
	
	